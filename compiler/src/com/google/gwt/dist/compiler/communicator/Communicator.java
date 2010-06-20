package com.google.gwt.dist.compiler.communicator;

import java.net.Socket;

import com.google.gwt.dist.Node;
import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.comm.CommMessagePayload;
import com.google.gwt.dist.comm.impl.ProcessingResultMessage;

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
	<T extends CommMessagePayload> T sendMessage(CommMessage<T> message,
			Node node);

	/**
	 * Retrieves data from the specified node.
	 * 
	 * @param n
	 *            Node from which to retrieve the data.
	 * @param message
	 *            M
	 * @return Retrieved data as byte array.
	 */
	byte[] retrieveData(ProcessingResultMessage message,
			Node n);

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
