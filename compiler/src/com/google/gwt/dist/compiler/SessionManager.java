package com.google.gwt.dist.compiler;

import java.io.InputStream;

import com.google.gwt.dist.Node;
import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.compiler.communicator.Communicator;

/**
 * Defines how SessionManager implementations will handle queries from client.
 */
public interface SessionManager {

	/**
	 * Check if CompilePerms is finished.
	 * 
	 * @param n
	 *            Node to check.
	 * 
	 * @return true if CompilePerms is completed and ready to be sent to the
	 *         client.
	 */
	boolean compilePermsCompleted(Node n);

	/**
	 * Fetch Communicator that is currently in use.
	 * 
	 * @return Currently used communicator.
	 */
	Communicator getCommunicator();

	/**
	 * Check if it is OK to send some data to the agent.
	 * 
	 * @param node
	 *            Note to which the data should be sent.
	 * @return true if it is OK for the client to send data to the agent. False
	 *         otherwise.
	 */
	boolean readyToReceiveData(Node node);

	/**s
	 * Sends data to specified node.
	 * 
	 * @param inputStream
	 *            Stream from which to read the data.
	 */
	void sendDataToClient(InputStream inputStream);

	/**
	 * Send CommMessage to the node.
	 * 
	 * @param message
	 *            CommMessage to be sent.
	 */
	void sendMessageToClient(CommMessage message);

	/**
	 * Sets communicator to be used.
	 * 
	 * @param communicator
	 *            Communicator to be used.
	 */
	void setCommunicator(Communicator communicator);
}
