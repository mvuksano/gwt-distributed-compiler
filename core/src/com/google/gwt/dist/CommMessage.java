package com.google.gwt.dist;

import java.io.Serializable;

import com.google.gwt.dist.SessionState.State;

/**
 * The definition for object that is used by client and agent for
 * intercommunication.
 */
public interface CommMessage extends Serializable {

	/**
	 * CommMessage Types.
	 */
	public enum CommMessageType {
		HELLO, ECHO
	}
	
	public State getSessionState();
	
	public CommMessageType getCommMessageType();
	
	public void setSessionState(State state);
}
