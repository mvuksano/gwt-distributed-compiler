package com.google.gwt.dist.compiler.agent.communicator.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.comm.CommMessageResponse;
import com.google.gwt.dist.comm.ProcessingStateResponse;
import com.google.gwt.dist.compiler.agent.DataProcessor;
import com.google.gwt.dist.compiler.agent.SessionManager;
import com.google.gwt.dist.compiler.agent.communicator.Communicator;
import com.google.gwt.dist.compiler.agent.communicator.InvalidOperationException;
import com.google.gwt.dist.compiler.agent.events.CompilePermsListener;
import com.google.gwt.dist.compiler.agent.events.DataReceivedListener;

public class CommunicatorImpl implements Communicator {

	private DataProcessor dataProcessor;
	private Set<CompilePermsListener> compilePermsFinishedListeners;
	private Set<DataReceivedListener> dataReceivedListeners;
	private InputStream is;
	private OutputStream os;
	private ServerSocket server;
	private SessionManager sessionManager;
	private static final Logger logger = Logger
			.getLogger(CommunicatorImpl.class.getName());

	public CommunicatorImpl(DataProcessor dataProcessor) {
		this.dataProcessor = dataProcessor;
		this.compilePermsFinishedListeners = new HashSet<CompilePermsListener>();
		this.dataReceivedListeners = new HashSet<DataReceivedListener>();
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
		this.dataProcessor.addListener(this.sessionManager);
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
				try {
					processData(receivedObject, this.sessionManager);
				} catch (InvalidOperationException e) {
				}
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

	/**
	 * Process data received from the client.
	 * 
	 * @param receivedData
	 *            Data received from the client as ByteArrayOutputStream.
	 * @param sessionManager
	 *            SessionManager that should be associated with processing.
	 *            TODO: This variable might be removed and the private property
	 *            could be used.
	 * @throws InvalidOperationException
	 */
	private void processData(ByteArrayOutputStream receivedData,
			SessionManager sessionManager) throws InvalidOperationException {
		try {
			logger.log(Level.INFO, "Starting processing data.");
			dataProcessor.storeInputStreamOnDisk(receivedData);
			dataProcessor.startCompilePerms();
		} catch (MalformedURLException e) {
		} catch (UnableToCompleteException e) {
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
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
	public void addCompilePermsListener(CompilePermsListener listener) {
		compilePermsFinishedListeners.add(listener);
	}

	@Override
	public void addDataReceivedListener(DataReceivedListener listener) {
		dataReceivedListeners.add(listener);
	}
}
