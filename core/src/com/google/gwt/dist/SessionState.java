package com.google.gwt.dist;

import java.io.Serializable;

/**
 * Describes state of a session that is going on between a client and an agent.
 */
public class SessionState implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2551246957349242327L;

	public static enum State {
		READY, INPROGRESS, COMPLETED, TERMINATED
	}

	private State state;

	public SessionState() {
		state = State.READY;
	}
	
	public SessionState(State state) {
		this.state = state;
	}

	public State getState() {
		return this.state;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	/**
	 * Switches state of the objects to the next one.
	 */
	public void switchState() {
		switch (this.state) {
			case READY : state = State.INPROGRESS; break;
			case INPROGRESS: state = State.COMPLETED; break;
			case COMPLETED: state = State.READY; break;
			default: break;
		}
			
	}

}
