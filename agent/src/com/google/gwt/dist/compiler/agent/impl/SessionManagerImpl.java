package com.google.gwt.dist.compiler.agent.impl;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Pattern;

import com.google.gwt.dist.ProcessingState;
import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.comm.CommMessagePayload;
import com.google.gwt.dist.comm.ProcessingStateResponse;
import com.google.gwt.dist.comm.ReturnResultPayload;
import com.google.gwt.dist.compiler.agent.SessionManager;
import com.google.gwt.dist.compiler.agent.communicator.Communicator;
import com.google.gwt.dist.compiler.agent.processor.DataProcessor;
import com.google.gwt.dist.impl.RequestProcessingResultMessage;
import com.google.gwt.dist.impl.SendDataMessage;
import com.google.gwt.dist.util.ZipCompressor;
import com.google.gwt.dist.util.ZipDecompressor;

/**
 * SessionManager handles sessions towards a node, for this agent.
 */
public class SessionManagerImpl implements SessionManager, Runnable {

	private Communicator communicator;
	private ZipCompressor compressor;
	private ZipDecompressor decompressor;
	private DataProcessor dataProcessor;

	public SessionManagerImpl() {
	}

	public SessionManagerImpl(DataProcessor dataProcessor) {
		this.dataProcessor = dataProcessor;
	}

	public Communicator getCommunicator() {
		return this.communicator;
	}

	public ZipCompressor getCompressor() {
		return this.compressor;
	}

	public ZipDecompressor getDecompressor() {
		return this.decompressor;
	}

	public ProcessingState getProcessingState() {
		return this.dataProcessor.getCurrentState();
	}

	public void processConnection(Socket client) {
		CommMessage<CommMessagePayload> message = communicator.getData(client);
		message.setResponse(decideResponse(message));
		communicator.sendData(message, client);
		communicator.closeConnection(client);
	}

	public void setCompressor(ZipCompressor compressor) {
		this.compressor = compressor;
	}

	public void setDecompressor(ZipDecompressor decompressor) {
		this.decompressor = decompressor;
	}

	public void setCommunicator(Communicator communicator) {
		this.communicator = communicator;
	}

	@SuppressWarnings("unchecked")
	private <T extends CommMessagePayload> T decideResponse(
			CommMessage<T> message) {
		T responseToReturn = null;
		switch (message.getCommMessageType()) {
		case DELIVERY_DATA:
			System.out.println("Data was delivered.");
			dataProcessor.onDataReceived(((SendDataMessage) message)
					.getResponse());
			break;
		case ECHO:
			responseToReturn = message.getResponse();
			break;
		case QUERY:
			responseToReturn = (T) new ProcessingStateResponse(
					getProcessingState());
			break;
		case RETURN_RESULT:
			try {
				ReturnResultPayload payload = new ReturnResultPayload();
				payload = ((RequestProcessingResultMessage) message)
						.getResponse();
				File folderFromWhichToPickData = new File(System
						.getProperty("user.dir")
						+ File.separator
						+ payload.getUUID()
						+ File.separator
						+ "work");
				byte[] data = compressor.archiveAndCompressDir(
						folderFromWhichToPickData,
						Pattern.compile("permutation-[0-9+].js")).toByteArray();
				payload.setResponseValue(data);
				responseToReturn = (T) payload;

				deleteDataStoredOnDisk(payload);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
		return responseToReturn;
	}

	/**
	 * Deletes data store on disk, for a compiler identified by uuid contained
	 * in payload.
	 * 
	 * @param payload
	 *            Payload which contains information about what data to delete.
	 */
	private void deleteDataStoredOnDisk(ReturnResultPayload payload) {
		deleteDir(new File(System.getProperty("user.dir") + File.separator
				+ payload.getUUID()));
		dataProcessor.reset();
	}
	
	//TODO: candidate for removal.
	@Override
	public void run() {
		System.out.println("running...");

	}
	
	/**
	 * Delete directory and all the files and folders in it.
	 * @param dir Directory to delete.
	 */
	private void deleteDir(File dir) {
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				deleteDir(f);
				f.delete();
			} else {
				f.delete();
			}
		}
	}
}