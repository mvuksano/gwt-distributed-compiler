package com.google.gwt.dist.compiler.agent.processor;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dev.CompilePerms;
import com.google.gwt.dev.util.log.PrintWriterTreeLogger;
import com.google.gwt.dist.ProcessingState;
import com.google.gwt.dist.compiler.agent.events.CompilePermsListener;
import com.google.gwt.dist.util.options.DistCompilePermsOptions;

/**
 * CompilePermsService executes actual CompilePerms operation. This is a long
 * running operation and is intended to be run in background. It uses observer
 * pattern model to notify other objects about a job being completed.
 */
public class CompilePermsService implements Runnable {

	private DistCompilePermsOptions options;
	private CompilePermsListener listener;
	private String uuid;

	public void compilePermsFinished() {
		listener.onDataProcessorStateChanged(ProcessingState.COMPLETED);
	}

	public CompilePermsListener getCompilePermsListener() {
		return this.listener;
	}

	public DistCompilePermsOptions getCompilePermsOptions() {
		return this.options;
	}

	@Override
	public void run() {
		ClassLoader prevClassLoader = null;
		try {
			List<URL> classpathURLs = new ArrayList<URL>();

			classpathURLs.add(new File(uuid + File.separator + "src"
					+ File.separator).toURI().toURL());

			File uncompressedLib = new File(uuid + File.separator + "lib"
					+ File.separator);

			for (File f : uncompressedLib.listFiles()) {
				classpathURLs.add(f.toURI().toURL());
			}

			TreeLogger logger = new PrintWriterTreeLogger();
			((PrintWriterTreeLogger) logger).setMaxDetail(TreeLogger.INFO);
			options.setWorkDir(new File(uuid + File.separator
					+ options.getWorkDir()));

			compilePermsStarted();

			prevClassLoader = Thread.currentThread().getContextClassLoader();
			URLClassLoader urlClassLoader = URLClassLoader.newInstance(
					classpathURLs.toArray(new URL[] {}), prevClassLoader);
			Thread.currentThread().setContextClassLoader(urlClassLoader);

			new CompilePerms(options).run(logger);
			System.gc();
			System.gc();
			Thread.currentThread().setContextClassLoader(prevClassLoader);
			urlClassLoader = null;
			classpathURLs = null;

			compilePermsFinished();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnableToCompleteException e) {
			e.printStackTrace();
		} finally {

		}

	}

	public void compilePermsStarted() {
		listener.onDataProcessorStateChanged(ProcessingState.INPROGRESS);
	}

	public void setCompilePermsListener(CompilePermsListener listener) {
		this.listener = listener;
	}

	public void setCompilePermsOptions(DistCompilePermsOptions options) {
		this.options = options;
	}

	public void initialize(DistCompilePermsOptions options, String uuid) {
		this.options = options;
		this.uuid = uuid;
	}

}
