package com.google.gwt.dist.compiler.agent.communicator;

import com.google.gwt.dist.compiler.agent.SessionManager;
import com.google.gwt.dist.compiler.agent.events.DataReceivedListener;

/**
 * This interface describes actual implementation of the transport layer used to
 * communicate with clients.
 */
public interface Communicator {

	/**
	 * Add listener that will be notified of data received event.
	 * 
	 * @param listener Listener to add to the list.
	 */
	void addDataReceivedListener(DataReceivedListener listener);
	
	SessionManager getSessionManager();
	
	void setSessionManager(SessionManager sessionManager);

	/**
	 * Start a server and listen to the specified port.
	 */
	void startServer();

	/**
	 * Stops the currently running server, if any.
	 */
	void stopServer();
}
