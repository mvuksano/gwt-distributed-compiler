package com.google.gwt.dist.compiler.agent.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dev.CompilePerms;
import com.google.gwt.dev.CompilePerms.CompilePermsOptions;
import com.google.gwt.dev.util.log.PrintWriterTreeLogger;
import com.google.gwt.dist.compiler.agent.DataProcessor;
import com.google.gwt.dist.compiler.agent.events.CompilePermsListener;
import com.google.gwt.dist.compiler.agent.events.DataReceivedListener;
import com.google.gwt.dist.util.Util;
import com.google.gwt.dist.util.ZipCompressor;
import com.google.gwt.dist.util.ZipDecompressor;

/**
 * Concrete implementation of the DataProcessor.
 */
public class DataProcessorImpl implements DataProcessor, DataReceivedListener,
		Runnable {

	private List<CompilePermsListener> compilePermsListeners;
	private ZipCompressor compressor;
	private ZipDecompressor decompressor;
	private File tempStorage;

	private static final Logger logger = Logger
			.getLogger(DataProcessorImpl.class.getName());

	/**
	 * Constructor.
	 * 
	 * @param decompressor
	 *            Decompressor used to uncompress the incoming stream.
	 * @param tempStorage
	 *            Directory that will be used as a temporary storage during
	 *            processing.
	 */
	public DataProcessorImpl(ZipCompressor compressor,
			ZipDecompressor decompressor, File tempStorage) {
		this.compilePermsListeners = new ArrayList<CompilePermsListener>();
		this.compressor = compressor;
		this.decompressor = decompressor;
		this.tempStorage = tempStorage;
	}

	/**
	 * Attach a CompilePermsListener.
	 */
	public void addListener(CompilePermsListener listener) {
		compilePermsListeners.add(listener);
	}

	/**
	 * Notify subscribers that CopilePerms is finished.
	 */
	public void compilePermsFinished() {
		for (CompilePermsListener l : compilePermsListeners) {
			l.onCompilePermsFinished();
		}
	}

	/**
	 * Detach a CompilePermsListener.
	 */
	public void removeListener(CompilePermsListener listener) {
		compilePermsListeners.remove(listener);
	}

	@Override
	public void startCompilePerms() throws UnableToCompleteException,
			MalformedURLException {
		File uncompressedSrc = new File(tempStorage + File.separator + "src"
				+ File.separator);
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
		int perms[] = { 0 };
		options.setPermsToCompile(perms);

		new CompilePerms(options).run(logger);
		compilePermsFinished();

		File dirToCompress = new File(tempStorage + File.separator);
		ByteArrayOutputStream compressedOutput = null;
		try {
			compressedOutput = compressor.archiveAndCompressDir(dirToCompress,
					Pattern.compile("permutation-[0-9+].js"));
			File zippedOutputFile = new File("test.zip");
			zippedOutputFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(zippedOutputFile);
			fos.write(compressedOutput.toByteArray());
			fos.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Stores input stream on disk.
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void storeInputStreamOnDisk(byte[] receivedData)
			throws FileNotFoundException, IOException {
		decompressor.decompressAndStoreToFile(receivedData, this.tempStorage);
	}

	@Override
	public void onDataReceived(byte[] receivedData) {
		try {
			storeInputStreamOnDisk(receivedData);
			startCompilePerms();
		} catch (MalformedURLException e) {
			logger.log(Level.INFO, e.getMessage());
		} catch (UnableToCompleteException e) {
			logger.log(Level.INFO, e.getMessage());
		} catch (FileNotFoundException e) {
			logger.log(Level.INFO, e.getMessage());
		} catch (IOException e) {
			logger.log(Level.INFO, e.getMessage());
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
