package com.google.gwt.dist.compiler.agent.processor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dev.CompilePerms;
import com.google.gwt.dev.CompilePerms.CompilePermsOptions;
import com.google.gwt.dev.util.log.PrintWriterTreeLogger;
import com.google.gwt.dist.ProcessingState;
import com.google.gwt.dist.compiler.agent.events.CompilePermsListener;
import com.google.gwt.dist.compiler.agent.impl.CompilePermsOptionsImpl;
import com.google.gwt.dist.util.Util;
import com.google.gwt.dist.util.ZipCompressor;

/**
 * CompilePermsService executes actual CompilePerms operation. This is a long
 * running operation and is intended to be run in background. It uses
 * publish/subscribe model to notify other objects about a job being completed.
 */
public class CompilePermsService implements Runnable {

	private ZipCompressor compressor;
	private CompilePermsListener listener;
	private File tempStorage;

	public CompilePermsService(CompilePermsListener listener) {
		this.compressor = new ZipCompressor();
		this.listener = listener;
		this.tempStorage = new File("uncompressed");
	}

	public void compilePermsFinished() {
		listener.onDataProcessorStateChanged(ProcessingState.COMPLETED);
	}

	@Override
	public void run() {
		try {
			File uncompressedSrc = new File(tempStorage + File.separator
					+ "src" + File.separator);

			Util.addUrl(uncompressedSrc.toURI().toURL());

			TreeLogger logger = new PrintWriterTreeLogger();
			((PrintWriterTreeLogger) logger).setMaxDetail(TreeLogger.INFO);

			// Compile Perms using the input data stored in tempStorage.
			List<String> moduleNames = new ArrayList<String>();
			moduleNames.add("com.hypersimple.HyperSimple");
			File workDir = new File("uncompressed" + File.separator + "work");

			final CompilePermsOptions options = new CompilePermsOptionsImpl();

			options.setModuleNames(moduleNames);
			options.setWorkDir(workDir);
			int perms[] = { 0, 1, 2, 3, 4, 5 };
			options.setPermsToCompile(perms);
			
			compilePermsStarted();
			new CompilePerms(options).run(logger);
			compilePermsFinished();

			File dirToCompress = new File(tempStorage + File.separator);
			ByteArrayOutputStream compressedOutput = null;
			compressedOutput = compressor.archiveAndCompressDir(dirToCompress,
					Pattern.compile("permutation-[0-9+].js"));
			File zippedOutputFile = new File("test.zip");
			zippedOutputFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(zippedOutputFile);
			fos.write(compressedOutput.toByteArray());
			fos.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnableToCompleteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void compilePermsStarted() {
		listener.onDataProcessorStateChanged(ProcessingState.INPROGRESS);
	}

}
