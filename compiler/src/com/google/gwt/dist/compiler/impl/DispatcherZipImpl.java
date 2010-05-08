package com.google.gwt.dist.compiler.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.google.gwt.dist.CommMessage;
import com.google.gwt.dist.Node;
import com.google.gwt.dist.compiler.Dispatcher;

/**
 * Implementation of Dispatcher service which archives and compresses all
 * necessary files and then emits that archive to the selected nodes.
 */
public class DispatcherZipImpl implements Dispatcher {

	@Override
	public void dispatchData(OutputStream data, Node... nodes) {
		for (Node n : nodes) {
			sendDataToNode(data, n);
		}

	}

	/**
	 * Sends data to remote location.
	 * 
	 * @param data
	 *            Data to send.
	 * @param node
	 *            {@link com.google.gwt.dist.Node} to which to send the data to.
	 * @return true if operation succeeded, false otherwise.
	 */
	private boolean sendDataToNode(OutputStream data, Node node) {
		InputStream is;
		OutputStream os;
		try {
			Socket server = new Socket(node.getIpaddress(), node.getPort());
			os = server.getOutputStream();
			is = server.getInputStream();
			
			os.write(((ByteArrayOutputStream)data).toByteArray());
			
			is.close();
			os.close();
			server.close();
		} catch (IOException e) {
		} 
		return false;
	}

}
