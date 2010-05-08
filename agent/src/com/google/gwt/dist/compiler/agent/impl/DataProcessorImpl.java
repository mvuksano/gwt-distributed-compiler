package com.google.gwt.dist.compiler.agent.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dev.CompilePerms;
import com.google.gwt.dev.CompilePerms.CompilePermsOptions;
import com.google.gwt.dev.util.log.PrintWriterTreeLogger;
import com.google.gwt.dist.compiler.agent.DataProcessor;
import com.google.gwt.dist.compiler.agent.events.CompilePermsListener;
import com.google.gwt.dist.util.Util;
import com.google.gwt.dist.util.ZipDecompressor;

/**
 * Concrete implementation of the DataProcessor.
 */
public class DataProcessorImpl implements DataProcessor {

	private List<CompilePermsListener> compilePermsListeners;
	private ZipDecompressor decompressor;
	private File tempStorage;

	/**
	 * Constructor.
	 * 
	 * @param decompressor
	 *            Decompressor used to uncompress the incoming stream.
	 * @param tempStorage
	 *            Directory that will be used as a temporary storage during
	 *            processing.
	 */
	public DataProcessorImpl(ZipDecompressor decompressor, File tempStorage) {
		this.compilePermsListeners = new ArrayList<CompilePermsListener>();
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
		File uncompressedSrc = new File("uncompressed" + File.separator + "src"
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
		int perms[] = { 0, 1, 2, 3, 4, 5 };
		options.setPermsToCompile(perms);

		new CompilePerms(options).run(logger);
		compilePermsFinished();
	}

	/**
	 * Stores input stream on disk.
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void storeInputStreamOnDisk(ByteArrayOutputStream receivedData)
			throws FileNotFoundException, IOException {
		decompressor.decompressAndStoreToFile(receivedData, this.tempStorage);
	}
}
