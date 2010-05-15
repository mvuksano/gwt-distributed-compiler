package com.google.gwt.dist.compiler.agent.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.google.gwt.dist.ProcessingState;
import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.comm.CommMessageResponse;
import com.google.gwt.dist.comm.ProcessingStateResponse;
import com.google.gwt.dist.comm.ReturnResultResponse;
import com.google.gwt.dist.compiler.agent.DataProcessor;
import com.google.gwt.dist.compiler.agent.SessionManager;
import com.google.gwt.dist.compiler.agent.communicator.Communicator;
import com.google.gwt.dist.impl.ProcessingStateMessage;
import com.google.gwt.dist.util.Util;
import com.google.gwt.dist.util.ZipDecompressor;

/**
 * SessionManager handles sessions towards a node, for this agent.
 */
public class SessionManagerImpl implements SessionManager, Runnable {

	private Communicator communicator;
	private DataProcessor dataProcessor;
	private ZipDecompressor decompressor;

	public SessionManagerImpl() {
	}

	public SessionManagerImpl(DataProcessor dataProcessor) {
		this.dataProcessor = dataProcessor;
	}

	public Communicator getCommunicator() {
		return this.communicator;
	}
	
	public ZipDecompressor getDecompressor() {
		return this.decompressor;
	}

	public ProcessingState getProcessingState() {
		return this.dataProcessor.getCurrentState();
	}

	public void processConnection(Socket client) {
		System.out
				.println("Started Thread " + Thread.currentThread().getName());
		System.out.println("Processing connection");
		try {
			byte[] receivedData = communicator.getData(client);
			if (isProcessingStateMessage(receivedData)) {
				ProcessingStateMessage message = getCommMessage(receivedData);
				message.setResponse(decideResponse(message));
				System.out.println(message.getCommMessageType() + ", " + message.getResponse().getCurrentState());
				communicator.sendData(Util.objectToByteArray(message), client);
			} else {
				File dirToStoreDataInto = new File("uncompressed");
				// Store data on disk.
				decompressor.decompressAndStoreToFile(receivedData,
						dirToStoreDataInto);
			}
			communicator.closeConnection(client);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished Thread "
				+ Thread.currentThread().getName());
	}
	
	public void setDecompressor (ZipDecompressor decompressor) {
		this.decompressor = decompressor;
	}

	public void setCommunicator(Communicator communicator) {
		this.communicator = communicator;
	}

	private ProcessingStateMessage getCommMessage(byte[] data) {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			ObjectInputStream ois = new ObjectInputStream(bis);
			return (ProcessingStateMessage) ois.readObject();
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private <T extends CommMessageResponse> T decideResponse(
			CommMessage<T> message) {
		T responseToReturn = null;
		switch (message.getCommMessageType()) {
		case ECHO:
			responseToReturn = message.getResponse();
			break;
		case QUERY:
			responseToReturn = (T) new ProcessingStateResponse(getProcessingState());
			break;
		case RETURN_RESULT:
			responseToReturn = (T) new ReturnResultResponse();
			break;
		}
		return responseToReturn;
	}

	private boolean isProcessingStateMessage(byte[] receivedData) {
		ProcessingStateMessage message = getCommMessage(receivedData);
		if (message != null) {
			return true;
		}
		return false;
	}

	@Override
	public void run() {
		System.out.println("running...");

	}
}