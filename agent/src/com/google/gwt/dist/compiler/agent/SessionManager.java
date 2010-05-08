package com.google.gwt.dist.compiler.agent;

import com.google.gwt.dist.SessionState;


/**
 * Interface that describes SessionManager which will perform within an agent.
 */
public interface SessionManager {
	
	/**
	 * Change session state to the one specified as parameter.
	 */
	void updateSessionState(SessionState state);
	
	/**
	 * Get current session state. 
	 */
	SessionState getState();
	
	/**
	 * Start listening for client requests.
	 */
	void startListening();
	
	/**
	 * Stop listening for client requests.
	 */
	void stopListening();
}
