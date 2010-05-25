package com.google.gwt.dist.compiler.agent.communicator.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.comm.CommMessagePayload;
import com.google.gwt.dist.comm.SendDataPayload;
import com.google.gwt.dist.compiler.agent.SessionManager;
import com.google.gwt.dist.compiler.agent.communicator.Communicator;
import com.google.gwt.dist.compiler.agent.events.DataReceivedListener;
import com.google.gwt.dist.util.ZipCompressor;

public class CommunicatorImpl implements Communicator {

	private ZipCompressor compressor;
	private Set<DataReceivedListener> dataReceivedListeners;
	private SessionManager sessionManager;

	private static final Logger logger = Logger
			.getLogger(CommunicatorImpl.class.getName());

	public CommunicatorImpl() {
		this.dataReceivedListeners = new HashSet<DataReceivedListener>();
		compressor = new ZipCompressor();
		compressor.setExcludePattern(Pattern
				.compile("bin|\\.settings\\.classpath\\.project"));
	}

	/**
	 * Closes a connection towards a client.
	 */
	public void closeConnection(Socket client) {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Notify DataReceivedListeners about data transfer being finished.
	 */
	public void dataReceived(SendDataPayload receivedData) {
		for (DataReceivedListener l : dataReceivedListeners) {
			l.onDataReceived(receivedData);
		}
	}

	@SuppressWarnings("unchecked")
	public CommMessage<CommMessagePayload> getData(Socket client) {
		logger.log(Level.INFO, "Getting data from client: "
				+ client.getInetAddress());
		CommMessage<CommMessagePayload> message = null;
		try {
			InputStream is = client.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			message = (CommMessage<CommMessagePayload>) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return message;
	}

	public SessionManager getSessionManager() {
		return this.sessionManager;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public void sendData(CommMessage<CommMessagePayload> message, Socket client) {
		try {
			OutputStream os = client.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(message);
			oos.flush();
			oos.close();
			os.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE,
					"There was a problem while sending data to client "
							+ client.getInetAddress());
		}
	}

	@Override
	public void serveClient(Socket client) {

	}

	@Override
	public void addDataReceivedListener(DataReceivedListener listener) {
		dataReceivedListeners.add(listener);
	}
}
