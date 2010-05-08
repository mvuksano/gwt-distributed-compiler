package com.google.gwt.dist.compiler;

import java.io.InputStream;

import com.google.gwt.dist.Node;
import com.google.gwt.dist.SessionState;
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
	
	Communicator getCommunicator();

	/**
	 * Request session state from the specified node.
	 * 
	 * @param node
	 * @return SessionState that the is in possession of the specified node.
	 */
	SessionState getSessionState(Node node);

	/**
	 * Check if it is OK to send some data to the agent.
	 * 
	 * @param node
	 *            Note to which the data should be sent.
	 * @return true if it is OK for the client to send data to the agent. False
	 *         otherwise.
	 */
	boolean readyToReceiveData(Node node);

	/**
	 * Sends data to specified node.
	 * 
	 * @param inputStream
	 *            Stream from which to read the data.
	 * @param node
	 *            Node to which to send the data.
	 */
	void sendDataToClient(InputStream inputStream, Node node);

	/**
	 * Sends SessionState object over the wire to the specified node.
	 */
	void sendSessionState(Node node, SessionState state);
	
	void setCommunicator(Communicator communicator);
}
