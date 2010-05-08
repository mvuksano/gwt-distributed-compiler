package com.google.gwt.dist.compiler.impl;

import java.io.InputStream;

import com.google.gwt.dist.Node;
import com.google.gwt.dist.SessionState;
import com.google.gwt.dist.SessionState.State;
import com.google.gwt.dist.compiler.SessionManager;
import com.google.gwt.dist.compiler.communicator.Communicator;

/**
 * Concrete SessionManager implementation.
 */
public class SessionManagerImpl implements SessionManager {

	private Communicator communicator;
	
	public SessionManagerImpl(Communicator communicator) {
		this.communicator = communicator;
	}
	
	@Override
	public boolean compilePermsCompleted(Node node) {
		if (communicator.getSessionState(node).getState() == State.COMPLETED) {
			return true;
		}
		return false;
	}

	@Override
	public boolean readyToReceiveData(Node node) {
		if (communicator.getSessionState(node).getState() == State.READY) {
			return true;
		}
		return false;
	}

	@Override
	public void sendDataToClient(InputStream inputStream, Node node) {
		communicator.sendData(inputStream, node);
	}

	@Override
	public SessionState getSessionState(Node node) {
		return communicator.getSessionState(node);
	}

	@Override
	public void sendSessionState(Node node, SessionState state) {
		communicator.sendSessionState(state, node);
	}

	@Override
	public Communicator getCommunicator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCommunicator(Communicator communicator) {
		// TODO Auto-generated method stub
		
	}
}
