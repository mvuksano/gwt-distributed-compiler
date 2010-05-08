package com.google.gwt.dist.compiler.communicator;

import java.io.InputStream;

import com.google.gwt.dist.Node;
import com.google.gwt.dist.SessionState;

/**
 * Provides abstraction layer for transport used by SessionManager.
 */
public interface Communicator {

	/**
	 * Get session state from the specified node.
	 * 
	 * @param node
	 *            Node from which to get the session state.
	 * @return SessionState that is in possession of Node.
	 */
	SessionState getSessionState(Node node);

	/**
	 * Send data to specified node.
	 * 
	 * @param inputStream
	 *            Stream from which to read the data.
	 * @param node
	 *            Node to which to send data.
	 */
	void sendData(InputStream inputStream, Node node);

	/**
	 * Send session state to the specified node;
	 * 
	 * @param state
	 *            Session state to send
	 * @param node
	 *            Node to which to sent the above mentioned state.
	 */
	void sendSessionState(SessionState state, Node node);

}
