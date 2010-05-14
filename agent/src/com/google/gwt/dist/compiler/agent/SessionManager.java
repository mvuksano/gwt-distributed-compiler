package com.google.gwt.dist.compiler.agent;

import java.net.Socket;

import com.google.gwt.dist.ProcessingState;
import com.google.gwt.dist.compiler.agent.communicator.Communicator;
import com.google.gwt.dist.compiler.agent.events.CompilePermsListener;


/**
 * Interface that describes SessionManager which will perform within an agent.
 */
public interface SessionManager extends CompilePermsListener {
	
	/**
	 * Gets communicator associated with session manager.
	 */
	Communicator getCommunicator();
	
	/**
	 * Get current processing state.
	 * @return
	 */
	ProcessingState getProcessingState();
	
	/**
	 * Process an accepted connection.
	 */
	void processConnection(Socket connection);
	
	/**
	 * Associates communicator with this session manager.
	 */
	void setCommunicator(Communicator communicator);
}
