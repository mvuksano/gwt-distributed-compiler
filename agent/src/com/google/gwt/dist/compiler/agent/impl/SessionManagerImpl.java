package com.google.gwt.dist.compiler.agent.impl;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Pattern;

import com.google.gwt.dist.ProcessingState;
import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.comm.CommMessageResponse;
import com.google.gwt.dist.comm.ProcessingStateResponse;
import com.google.gwt.dist.comm.ReturnResultResponse;
import com.google.gwt.dist.compiler.agent.SessionManager;
import com.google.gwt.dist.compiler.agent.communicator.Communicator;
import com.google.gwt.dist.compiler.agent.processor.DataProcessor;
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
		CommMessage<CommMessageResponse> message = communicator.getData(client);
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
	private <T extends CommMessageResponse> T decideResponse(
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
				ReturnResultResponse response = new ReturnResultResponse();
				File folderFromWhichToPickData = new File(System
						.getProperty("user.dir")
						+ File.separator
						+ "uncompressed"
						+ File.separator
						+ "work");
				byte[] data = compressor.archiveAndCompressDir(
						folderFromWhichToPickData,
						Pattern.compile("permutation-[0-9+].js")).toByteArray();
				response.setResponseValue(data);
				responseToReturn = (T) response;
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
		return responseToReturn;
	}

	@Override
	public void run() {
		System.out.println("running...");

	}
}