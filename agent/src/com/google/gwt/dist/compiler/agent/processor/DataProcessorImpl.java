package com.google.gwt.dist.compiler.agent.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dist.ProcessingState;
import com.google.gwt.dist.compiler.agent.events.CompilePermsListener;
import com.google.gwt.dist.compiler.agent.events.DataReceivedListener;
import com.google.gwt.dist.util.ZipDecompressor;

/**
 * Concrete implementation of the DataProcessor.
 */
public class DataProcessorImpl implements CompilePermsListener, DataProcessor,
		DataReceivedListener, Runnable {

	private ZipDecompressor decompressor;
	private ProcessingState state;
	private File tempStorage;

	private ExecutorService executorService;

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
	public DataProcessorImpl(ZipDecompressor decompressor, File tempStorage) {
		this.decompressor = decompressor;
		this.tempStorage = tempStorage;
		state = ProcessingState.READY;
		executorService = Executors.newFixedThreadPool(5);
	}
	
	@Override
	public ProcessingState getCurrentState() {
		return this.state;
	}
	
	@Override
	public void onDataProcessorStateChanged(ProcessingState state) {
		this.state = state;
	}

	@Override
	public void onDataReceived(byte[] receivedData) {
		try {
			storeInputStreamOnDisk(receivedData);
			executorService.execute(new CompilePermsService(this));
			executorService.shutdown();
		} catch (MalformedURLException e) {
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
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
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
}