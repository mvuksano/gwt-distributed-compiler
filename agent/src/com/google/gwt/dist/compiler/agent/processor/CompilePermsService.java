package com.google.gwt.dist.compiler.agent.processor;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dev.CompilePerms;
import com.google.gwt.dev.CompilePerms.CompilePermsOptions;
import com.google.gwt.dev.util.log.PrintWriterTreeLogger;
import com.google.gwt.dist.ProcessingState;
import com.google.gwt.dist.compiler.agent.events.CompilePermsListener;

/**
 * CompilePermsService executes actual CompilePerms operation. This is a long
 * running operation and is intended to be run in background. It uses
 * publish/subscribe model to notify other objects about a job being completed.
 */
public class CompilePermsService implements Runnable {

	private CompilePermsOptions options;
	private CompilePermsListener listener;
	private String uuid;

	public void compilePermsFinished() {
		listener.onDataProcessorStateChanged(ProcessingState.COMPLETED);
	}

	public CompilePermsListener getCompilePermsListener() {
		return this.listener;
	}

	public CompilePermsOptions getCompilePermsOptions() {
		return this.options;
	}

	@Override
	public void run() {
		try {
			File uncompressedSrc = new File(uuid + File.separator + "src"
					+ File.separator);

			File uncompressedLib = new File(uuid + File.separator + "lib"
					+ File.separator);

			TreeLogger logger = new PrintWriterTreeLogger();
			((PrintWriterTreeLogger) logger).setMaxDetail(TreeLogger.INFO);

			options.setWorkDir(new File(uuid + File.separator
					+ options.getWorkDir()));

			compilePermsStarted();
			
			ClassLoader prevClassLoader = Thread.currentThread().getContextClassLoader();
			URLClassLoader urlClassLoader = new URLClassLoader(new URL[] {
					uncompressedSrc.toURI().toURL(),
					uncompressedLib.toURI().toURL() });
			
			Thread.currentThread().setContextClassLoader(urlClassLoader);
			
			new CompilePerms(options).run(logger);
			
			Thread.currentThread().setContextClassLoader(prevClassLoader);
			compilePermsFinished();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnableToCompleteException e) {
			e.printStackTrace();
		}

	}

	public void compilePermsStarted() {
		listener.onDataProcessorStateChanged(ProcessingState.INPROGRESS);
	}

	public void setCompilePermsListener(CompilePermsListener listener) {
		this.listener = listener;
	}

	public void setCompilePermsOptions(CompilePermsOptions options) {
		this.options = options;
	}

	public void initialize(CompilePermsOptions options, String uuid) {
		this.options = options;
		this.uuid = uuid;
	}

}
