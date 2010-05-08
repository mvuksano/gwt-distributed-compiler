package com.google.gwt.dist.compiler.agent.communicator.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dist.CommMessage;
import com.google.gwt.dist.SessionState;
import com.google.gwt.dist.SessionState.State;
import com.google.gwt.dist.compiler.agent.SessionManager;
import com.google.gwt.dist.compiler.agent.communicator.Communicator;
import com.google.gwt.dist.compiler.agent.communicator.InvalidOperationException;
import com.google.gwt.dist.compiler.agent.events.CompilePermsListener;
import com.google.gwt.dist.compiler.agent.events.DataReceivedListener;
import com.google.gwt.dist.util.ZipDecompressor;

public class CommunicatorImpl implements Communicator {

	private Set<CompilePermsListener> compilePermsFinishedListeners;
	private Set<DataReceivedListener> dataReceivedListeners;
	private ZipDecompressor decompressor;
	private InputStream is;
	private OutputStream os;
	private ServerSocket server;
	private SessionManager sessionManager;
	private File temporaryStorage = null; // Store source and precompile output.
	private static final Logger logger = Logger
			.getLogger(CommunicatorImpl.class.getName());

	public CommunicatorImpl(ZipDecompressor decompressor, File tempStorage) {
		this.decompressor = decompressor;
		this.temporaryStorage = tempStorage;
		this.compilePermsFinishedListeners = new HashSet<CompilePermsListener>();
		this.dataReceivedListeners = new HashSet<DataReceivedListener>();
	}

	public ServerSocket getServer() {
		return this.server;
	}

	public SessionManager getSessionManager() {
		return this.sessionManager;
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
			byte[] buff = new byte[2056];
			int bytesRead = 0;
			ByteArrayOutputStream receivedObject = new ByteArrayOutputStream();
			while ((bytesRead = is.read(buff)) > -1) {
				receivedObject.write(buff, 0, bytesRead);
			}

			// Check if received stream is CommMessage or not contents.
			CommMessage commMessage = getCommMessage(receivedObject);
			if (commMessage != null) {
				// It's a comm message.
				System.out.println(commMessage);
			} else {
				// it's a file stream.
				processData(receivedObject);
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} catch (InvalidOperationException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} finally {
			try {
				is.close();
				os.close();
				client.close();
				server.close();
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
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
	 * 
	 * @param is
	 *            InputStream from which to read the data.
	 * @param os
	 *            OutputStream to which to write the data.
	 * @throws InvalidOperationException
	 */
	private void processData(ByteArrayOutputStream receivedData)
			throws InvalidOperationException {
		logger.log(Level.INFO, "Starting processing data.");
		sessionManager.getState().setState(State.READY);

		if (sessionManager.getState().getState() == State.READY) {
			sessionManager
					.updateSessionState(new SessionState(State.INPROGRESS));
			storeInputStreamOnDisk(receivedData, this.temporaryStorage);
		} else {
			throw new InvalidOperationException(
					"Session must be in state READY before sending data for processing.");
		}
	}

	/**
	 * Stores input stream on disk.
	 */
	private void storeInputStreamOnDisk(ByteArrayOutputStream receivedData,
			File location) {
		decompressor.decompressAndStoreToFile(receivedData, location);
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
	public boolean workFinished() {
		if (sessionManager.getState().getState() == State.COMPLETED)
			return true;
		return false;
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
