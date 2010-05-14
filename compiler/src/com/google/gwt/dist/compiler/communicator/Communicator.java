package com.google.gwt.dist.compiler.communicator;

import java.net.Socket;

import com.google.gwt.dist.Node;
import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.comm.CommMessageResponse;

/**
 * Provides abstraction layer for transport used by SessionManager.
 */
public interface Communicator {
	
	Socket getClient();

	/**
	 * Send Communication Message to the agent.
	 * 
	 * @param node
	 *            Node which state should be retrieved.
	 * @param message
	 *            CommMessage which contains the query.
	 * @return CommMessageResponse that is wrapped in the message.
	 */
	<T extends CommMessageResponse> T sendMessage(CommMessage<T> message, Node node);

	/**
	 * Send data to specified node.
	 * 
	 * @param data
	 *            Data to be sent, already converted into byte array.
	 * @param node
	 *            Node to which to send data.
	 */
	void sendData(byte[] data, Node node);
	
	void setClient(Socket client);

}
