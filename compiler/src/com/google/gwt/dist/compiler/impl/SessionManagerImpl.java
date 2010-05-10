package com.google.gwt.dist.compiler.impl;

import java.io.InputStream;

import com.google.gwt.dist.Node;
import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.compiler.SessionManager;
import com.google.gwt.dist.compiler.communicator.Communicator;

/**
 * Concrete SessionManager implementation.
 */
public class SessionManagerImpl implements SessionManager {

	private Communicator communicator;
	private Node node;

	public SessionManagerImpl(Communicator communicator, Node node) {
		this.communicator = communicator;
		this.node = node;
	}

	@Override
	public boolean compilePermsCompleted(Node node) {
		return false;
	}

	@Override
	public boolean readyToReceiveData(Node node) {
		return false;
	}

	@Override
	public void sendDataToClient(InputStream inputStream) {
		communicator.sendData(inputStream, node);
	}
	
	public void sendMessageToClient(CommMessage message) {
		communicator.sendMessage(message, this.node);
	}

	@Override
	public Communicator getCommunicator() {
		return this.communicator;
	}

	@Override
	public void setCommunicator(Communicator communicator) {
		this.communicator = communicator;
	}
}
