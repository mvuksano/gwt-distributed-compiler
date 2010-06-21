package com.google.gwt.dist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.ArgProcessorBase;
import com.google.gwt.dev.util.arg.ArgHandlerLogLevel;
import com.google.gwt.dev.util.arg.ArgHandlerModuleName;
import com.google.gwt.dev.util.arg.ArgHandlerTreeLoggerFlag;
import com.google.gwt.dev.util.arg.ArgHandlerWorkDirRequired;
import com.google.gwt.dist.compiler.Precompile;
import com.google.gwt.dist.compiler.communicator.Communicator;
import com.google.gwt.dist.compiler.communicator.Distributor;
import com.google.gwt.dist.compiler.impl.SessionManagerImpl;
import com.google.gwt.dist.linker.Link;
import com.google.gwt.dist.util.ZipCompressor;
import com.google.gwt.dist.util.ZipDecompressor;
import com.google.gwt.dist.util.arg.ArgHandlerGWTClassPath;
import com.google.gwt.dist.util.arg.ArgHandlerNodes;
import com.google.gwt.dist.util.arg.ArgHandlerUUIDFile;
import com.google.gwt.dist.util.options.DistCompilePermsOptions;
import com.google.gwt.dist.util.options.DistCompilePermsOptionsImpl;
import com.google.gwt.dist.util.options.DistCompilerOptions;
import com.google.gwt.dist.util.options.LinkOptionsImpl;
import com.google.gwt.dist.util.options.PrecompileOptionsImpl;

public class Application {

	/**
	 * As the name suggests, DistCompilerArgProcessor is used to process
	 * arguments passed through command line.
	 */
	static class DistCompilerArgProcessor extends ArgProcessorBase {
		public DistCompilerArgProcessor(DistCompilerOptions options) {
			registerHandler(new ArgHandlerLogLevel(options));
			registerHandler(new ArgHandlerTreeLoggerFlag(options));
			registerHandler(new ArgHandlerWorkDirRequired(options));
			registerHandler(new ArgHandlerGWTClassPath(options));
			registerHandler(new ArgHandlerNodes(options));
			registerHandler(new ArgHandlerUUIDFile(options));
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
	private TreeLogger treeLogger;

	private static final Logger logger = Logger.getLogger(Application.class);

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
		DistCompilerOptions options = (DistCompilerOptions) appContext
				.getBean("distOptions");
		if (new DistCompilerArgProcessor(options).processArgs(args)) {
			app.start(options);
		}
	}

	public void start(DistCompilerOptions options) {
		List<Node> nodes = options.getNodes();
		List<SessionManager> sessionManagers = new ArrayList<SessionManager>();
		PrecompileOptionsImpl precompileOptions = new PrecompileOptionsImpl(
				options);
		Precompile precompile = new Precompile(precompileOptions);
		long compileStart = System.currentTimeMillis();
		precompile.run(treeLogger);

		int permsToCompile[] = getPermsToCompile(precompileOptions.getWorkDir());

		DistCompilePermsOptions compilePermsOptions = new DistCompilePermsOptionsImpl(
				options);
		compilePermsOptions.setPermsToCompile(permsToCompile);
		Map<Node, int[]> distributionMatrix = distributor.distribute(
				compilePermsOptions.getPermsToCompile(), nodes);

		for (Node n : nodes) {
			DistCompilePermsOptions customizedCompilePermsOptions = new DistCompilePermsOptionsImpl(
					options);
			customizedCompilePermsOptions.setPermsToCompile(distributionMatrix
					.get(n));
			sessionManagers.add(new SessionManagerImpl(communicator, n,
					customizedCompilePermsOptions, compressor, decompressor));
		}

		while (!allSessionManagersFinished(sessionManagers)) {
			for (SessionManager sm : sessionManagers) {
				if (!sm.isFinished()) {
					sm.start();
				}
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
		}

		LinkOptionsImpl linkOptions = new LinkOptionsImpl(options);
		Link link = new Link(linkOptions);
		link.run(treeLogger);

		long compileFinish = System.currentTimeMillis();
		long delta = compileFinish - compileStart;
//		treeLogger.log(TreeLogger.INFO, "Compilation succeeded -- "
//				+ String.format("%.3f", delta / 1000d) + "s");
		logger.info("Compilation succeeded -- "
				+ String.format("%.3f", delta / 1000d) + "s");
	}

	/**
	 * Check if correct settings are present.
	 * 
	 * @param settings
	 *            Settings to be checked.
	 * @return True if correct settings were present. False otherwise.
	 */
	protected boolean validUUID(AgentsSettings settings) {
		if (settings.getUUID().equals("")) {
			settings.setUUID(UUID.randomUUID().toString());
			return false;
		}
		return true;
	}

	protected boolean allSessionManagersFinished(
			List<SessionManager> sessionManagers) {
		boolean finished = true;
		for (SessionManager s : sessionManagers) {
			if (s.isFinished() == Boolean.valueOf(false)) {
				finished = false;
			}
		}
		return finished;
	}

	/**
	 * Reads how many permutations are there to be compiled.
	 * 
	 * @param dir
	 *            Directory to which percompile step has emitted its output.
	 * @return int array which contains permutations which should be compiled.
	 */
	private int[] getPermsToCompile(File dir) {
		return new int[] { 0, 1, 2, 3, 4, 5 };
	}
}
