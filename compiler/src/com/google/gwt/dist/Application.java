package com.google.gwt.dist;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.stream.StreamSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.ArgProcessorBase;
import com.google.gwt.dev.CompileTaskOptions;
import com.google.gwt.dev.CompilerOptions;
import com.google.gwt.dev.CompilePerms.CompilePermsOptions;
import com.google.gwt.dev.util.arg.ArgHandlerLogLevel;
import com.google.gwt.dev.util.arg.ArgHandlerModuleName;
import com.google.gwt.dev.util.arg.ArgHandlerTreeLoggerFlag;
import com.google.gwt.dev.util.arg.ArgHandlerWorkDirRequired;
import com.google.gwt.dist.compiler.Precompile;
import com.google.gwt.dist.compiler.communicator.Communicator;
import com.google.gwt.dist.compiler.communicator.Distributor;
import com.google.gwt.dist.compiler.impl.SessionManagerImpl;
import com.google.gwt.dist.link.LinkOptionsImpl;
import com.google.gwt.dist.linker.Link;
import com.google.gwt.dist.perms.CompilePermsOptionsImpl;
import com.google.gwt.dist.precompile.PrecompileOptionsImpl;
import com.google.gwt.dist.util.ZipCompressor;
import com.google.gwt.dist.util.ZipDecompressor;

public class Application {

	/**
	 * As the name suggests, DistCompilerArgProcessor is used to process
	 * arguments passed through command line.
	 */
	static class DistCompilerArgProcessor extends ArgProcessorBase {
		public DistCompilerArgProcessor(CompileTaskOptions options) {
			registerHandler(new ArgHandlerLogLevel(options));
			registerHandler(new ArgHandlerTreeLoggerFlag(options));
			registerHandler(new ArgHandlerWorkDirRequired(options));
			registerHandler(new ArgHandlerModuleName(options) {
				@Override
				public String getPurpose() {
					return super.getPurpose() + " to compile";
				}
			});
		}

		@Override
		protected String getName() {
			return DistCompilerArgProcessor.class.getName();
		}
	}

	private Communicator communicator;
	private ZipCompressor compressor;
	private ZipDecompressor decompressor;
	private Distributor distributor;
	private Marshaller marshaller;
	private TreeLogger treeLogger;
	private Unmarshaller unmarshaller;
	private AgentsSettings settings;
	private static String AGENTS_SETTINGS_FILE_LOCATION = "config.xml";

	private static final Logger logger = Logger.getLogger(Application.class
			.getName());

	public Application(Communicator communicator, ZipCompressor compressor,
			ZipDecompressor decompressor, Distributor distributor,
			TreeLogger treeLogger) {
		this.communicator = communicator;
		this.compressor = compressor;
		this.decompressor = decompressor;
		this.distributor = distributor;
		this.treeLogger = treeLogger;
	}

	public static void main(String[] args) {
		ApplicationContext appContext = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		Application app = (Application) appContext.getBean("application");
		app.loadSettings();
		CompilerOptions options = (CompilerOptions) appContext
				.getBean("compilerOptions");
		if (new DistCompilerArgProcessor(options).processArgs(args)) {
			app.start(options);
		}
	}

	public void start(CompilerOptions options) {

		List<Node> nodes = this.settings.getNodes();
		List<SessionManager> sessionManagers = new ArrayList<SessionManager>();
		PrecompileOptionsImpl precompileOptions = new PrecompileOptionsImpl(
				options);
		Precompile precompile = new Precompile(precompileOptions);
		precompile.run(treeLogger);

		CompilePermsOptions compilePermsOptions = new CompilePermsOptionsImpl(
				options);
		compilePermsOptions.setPermsToCompile(new int[] { 0, 1, 2, 3, 4, 5 });
		Map<Node, int[]> distributionMatrix = distributor.distribute(
				compilePermsOptions.getPermsToCompile(), nodes);

		for (Node n : nodes) {
			CompilePermsOptions customizedCompilePermsOptions = new CompilePermsOptionsImpl(
					options);
			customizedCompilePermsOptions.setPermsToCompile(distributionMatrix
					.get(n));
			sessionManagers.add(new SessionManagerImpl(communicator, n,
					customizedCompilePermsOptions, compressor, decompressor));
		}

		Map<SessionManager, Boolean> sessionManagerStatusList = initializeSessionManagerStatusList(sessionManagers);

		while (!allSessionManagersFinished(sessionManagers,
				sessionManagerStatusList)) {
			for (SessionManager sm : sessionManagers) {
				sessionManagerStatusList.put(sm, sm.start());
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
			}
		}

		LinkOptionsImpl linkOptions = new LinkOptionsImpl(options);
		Link link = new Link(linkOptions);
		link.run(treeLogger);
	}

	public Marshaller getMarshaller() {
		return this.marshaller;
	}

	public AgentsSettings getSettings() {
		return this.settings;
	}

	public Unmarshaller getUnmarshaller() {
		return this.unmarshaller;
	}

	/**
	 * Loads settings from Application.AGENTS_SETTINGS_FILE_LOCATION TODO:
	 * Candidate for refactoring - File might be supplied as parameter.
	 */
	protected AgentsSettings loadSettings() {
		InputStream is = null;
		try {
			is = new ClassPathResource(AGENTS_SETTINGS_FILE_LOCATION)
					.getInputStream();
			this.settings = (AgentsSettings) this.unmarshaller
					.unmarshal(new StreamSource(is));
		} catch (XmlMappingException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, e.getMessage());
				}
			}
		}
		return settings;
	}

	public void setMarshaller(Marshaller m) {
		this.marshaller = m;
	}

	public void setUnmarshaller(Unmarshaller um) {
		this.unmarshaller = um;
	}

	protected boolean allSessionManagersFinished(
			List<SessionManager> sessionManagers,
			Map<SessionManager, Boolean> sessionManagerStatusList) {
		boolean finished = true;
		for (SessionManager s : sessionManagers) {
			if (sessionManagerStatusList.get(s) == Boolean.valueOf(false)) {
				finished = false;
			}
		}
		return finished;
	}

	private Map<SessionManager, Boolean> initializeSessionManagerStatusList(
			List<SessionManager> sessionManagers) {
		Map<SessionManager, Boolean> sessionManagerStatusList = new HashMap<SessionManager, Boolean>();
		for (SessionManager sm : sessionManagers) {
			sessionManagerStatusList.put(sm, Boolean.valueOf(false));
		}
		return sessionManagerStatusList;
	}
}
