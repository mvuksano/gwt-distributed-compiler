package com.google.gwt.dist.compiler.agent.impl;

import com.google.gwt.dist.SessionState;
import com.google.gwt.dist.SessionState.State;
import com.google.gwt.dist.compiler.agent.SessionManager;
import com.google.gwt.dist.compiler.agent.communicator.Communicator;

/**
 * SessionManager handles SessionState object for this agent.
 */
public class SessionManagerImpl implements SessionManager {

	private Communicator communicator;
	private SessionState state;
	
	public SessionManagerImpl() {
		state = new SessionState(State.READY);
	}
	
	public Communicator getCommunicator() {
		return this.communicator;
	}
	
	public SessionState getState() {
		return this.state;
	}
	
	public void setCommunicator(Communicator communicator) {
		this.communicator  = communicator;
	}
	
	public void setState(SessionState state) {
		this.state = state;
	}
	
	public void startListening() {
		communicator.startServer();
	}
	
	public void stopListening() {
		communicator.stopServer();
	}

	@Override
	public void updateSessionState(SessionState state) {
		this.state = state;
	}

}