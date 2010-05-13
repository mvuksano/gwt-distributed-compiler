package com.google.gwt.dist.compiler.agent.communicator.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.comm.CommMessageResponse;
import com.google.gwt.dist.comm.ProcessingStateResponse;
import com.google.gwt.dist.compiler.agent.SessionManager;
import com.google.gwt.dist.compiler.agent.communicator.Communicator;
import com.google.gwt.dist.compiler.agent.events.DataReceivedListener;
import com.google.gwt.dist.util.ZipCompressor;

public class CommunicatorImpl implements Communicator {

	private Set<DataReceivedListener> dataReceivedListeners;
	private ZipCompressor compressor;
	private InputStream is;
	private OutputStream os;
	private ServerSocket server;
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
	 * Notify DataReceivedListeners about data transfer being finished.
	 */
	public void dataReceived(byte[] receivedData) {
		for (DataReceivedListener l : dataReceivedListeners) {
			l.onDataReceived(receivedData);
		}
	}

	public ServerSocket getServer() {
		return this.server;
	}

	public SessionManager getSessionManager() {
		return this.sessionManager;
	}

	/**
	 * Processes the incoming CommMessage and returns a modified one.
	 * 
	 * @param message
	 *            The message to process.
	 * @return Updated message.
	 */
	public CommMessage processCommMessage(CommMessage message) {
		CommMessageResponse response = new ProcessingStateResponse();
		((ProcessingStateResponse) response)
				.setCurrentState(this.sessionManager.getProcessingState());
		message.setResponse(response);
		return message;
	}

	public void setServer(ServerSocket serverSocket) {
		this.server = serverSocket;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	@Override
	public void startServer() {
		Socket client = null;
		try {
			server = new ServerSocket(3000);
			logger.log(Level.INFO, "Waiting for connections.");
			client = server.accept();
			logger.log(Level.INFO, "Accepted a connection from: "
					+ client.getInetAddress());
			os = client.getOutputStream();
			is = client.getInputStream();

			// Read contents of the stream and store it into a byte array.
			byte[] buff = new byte[512];
			int bytesRead = 0;
			ByteArrayOutputStream receivedObject = new ByteArrayOutputStream();
			while ((bytesRead = is.read(buff)) > -1) {
				receivedObject.write(buff, 0, bytesRead);
			}

			// Check if received stream is CommMessage or not.
			CommMessage commMessage = getCommMessage(receivedObject);
			if (commMessage != null) {
				commMessage = processCommMessage(commMessage);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bos);
				oos.writeObject(commMessage);
				os.write(bos.toByteArray());
			} else {
				dataReceived(receivedObject.toByteArray());
			}
			client.shutdownOutput();
			is.close();
			os.close();
			client.close();
			server.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	private CommMessage getCommMessage(ByteArrayOutputStream baos) {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(baos
					.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bis);
			return (CommMessage) ois.readObject();
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		}
		return null;
	}

	@Override
	public void stopServer() {
		try {
			server.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	@Override
	public void addDataReceivedListener(DataReceivedListener listener) {
		dataReceivedListeners.add(listener);
	}
}
