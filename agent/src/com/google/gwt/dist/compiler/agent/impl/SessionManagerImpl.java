package com.google.gwt.dist.compiler.agent.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.google.gwt.dist.ProcessingState;
import com.google.gwt.dist.comm.ProcessingStateResponse;
import com.google.gwt.dist.compiler.agent.SessionManager;
import com.google.gwt.dist.compiler.agent.communicator.Communicator;
import com.google.gwt.dist.compiler.agent.events.CompilePermsListener;
import com.google.gwt.dist.impl.ProcessingStateMessage;
import com.google.gwt.dist.util.Util;

/**
 * SessionManager handles sessions towards a node, for this agent.
 */
public class SessionManagerImpl implements SessionManager, CompilePermsListener {

	private Communicator communicator;
	private ProcessingState processingState;

	public SessionManagerImpl() {
		this.processingState = ProcessingState.READY;
	}

	public Communicator getCommunicator() {
		return this.communicator;
	}

	public ProcessingState getProcessingState() {
		return this.processingState;
	}

	public void onCompilePermsFinished() {
		this.processingState = ProcessingState.COMPLETED;
	}

	public void onCompilePermsStarted() {
		this.processingState = ProcessingState.INPROGRESS;
	}

	public void processConnection(Socket client) {
		System.out.println("Processing connection");
		byte[] receivedData = communicator.getData(client);
		if (isProcessingStateMessage(receivedData)) {
			ProcessingStateMessage message = getCommMessage(receivedData);
			message.setResponse(new ProcessingStateResponse());
			System.out.println(message.getCommMessageType());
			communicator.sendData(Util.objectToByteArray(message), client);
		}
		communicator.closeConnection(client);
	}

	public void setProcessingState(ProcessingState processingState) {
		this.processingState = processingState;
	}

	public void setCommunicator(Communicator communicator) {
		this.communicator = communicator;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

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

	private boolean isProcessingStateMessage(byte[] receivedData) {
		ProcessingStateMessage message = getCommMessage(receivedData);
		if (message != null) {
			return true;
		}
		return false;
	}
}