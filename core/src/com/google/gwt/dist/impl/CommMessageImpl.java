package com.google.gwt.dist.impl;

import com.google.gwt.dist.CommMessage;
import com.google.gwt.dist.SessionState.State;

public class CommMessageImpl implements CommMessage {

	private static final long serialVersionUID = -6282502951222110241L;
	
	private CommMessageType commMessageType;
	private State sessionState;
	
	public CommMessageImpl() {
		commMessageType = CommMessageType.ECHO;
		sessionState = null;
	}
	
	public CommMessageType getCommMessageType() {
		return this.commMessageType;
	}
	
	public State getSessionState() {
		return sessionState;
	}
	
	public void setSessionState(State sessionState) {
		this.sessionState = sessionState;
	}
}
