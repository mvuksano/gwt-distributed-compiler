package com.google.gwt.dist.compiler.agent.communicator;

import java.net.Socket;

import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.comm.CommMessageResponse;
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
	
	/**
	 * Close a connection towards a client.
	 * @param client
	 */
	void closeConnection(Socket client);
	
	/**
	 * Gets data from a client
	 * @return
	 */
	CommMessage<CommMessageResponse> getData(Socket client);
	
	SessionManager getSessionManager();
	
	/**
	 * Send data towards a client. 
	 * @param client
	 */
	void sendData(CommMessage<CommMessageResponse> message, Socket client);
	
	void setSessionManager(SessionManager sessionManager);

	/**
	 * Start a server and listen to the specified port.
	 */
	void serveClient(Socket client);
}
