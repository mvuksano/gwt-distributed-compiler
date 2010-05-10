package com.google.gwt.dist.compiler.communicator;

import java.io.InputStream;

import com.google.gwt.dist.Node;
import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.comm.CommMessageResponse;

/**
 * Provides abstraction layer for transport used by SessionManager.
 */
public interface Communicator {

	/**
	 * Send Communication Message to the agent.
	 * 
	 * @param node
	 *            Node which state should be retrieved.
	 * @param message
	 *            CommMessage which contains the query.
	 * @return SessionState that is in possession of Node.
	 */
	CommMessageResponse sendMessage(CommMessage message, Node node);

	/**
	 * Send data to specified node.
	 * 
	 * @param inputStream
	 *            Stream from which to read the data.
	 * @param node
	 *            Node to which to send data.
	 */
	void sendData(InputStream inputStream, Node node);

}
