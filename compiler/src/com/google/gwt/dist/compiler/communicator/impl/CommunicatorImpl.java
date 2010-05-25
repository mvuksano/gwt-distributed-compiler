package com.google.gwt.dist.compiler.communicator.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dist.Node;
import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.comm.CommMessagePayload;
import com.google.gwt.dist.comm.ReturnResultPayload;
import com.google.gwt.dist.compiler.communicator.Communicator;
import com.google.gwt.dist.impl.RequestProcessingResultMessage;

public class CommunicatorImpl implements Communicator {

	private Socket client;

	private static final Logger logger = Logger
			.getLogger(CommunicatorImpl.class.getName());

	public Socket getClient() {
		return this.client;
	}

	@Override
	public void sendData(byte[] data, Node node) {
		Socket server;
		try {
			server = new Socket(node.getIpaddress(), node.getPort());
			InputStream is = server.getInputStream();
			OutputStream os = server.getOutputStream();

			os.write(data);
			server.shutdownOutput();

			os.close();
			is.close();
			server.close();
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends CommMessagePayload> T sendMessage(CommMessage<T> message,
			Node node) {
		try {
			Socket server = new Socket(node.getIpaddress(), node.getPort());
			InputStream is = server.getInputStream();
			OutputStream os = server.getOutputStream();

			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(message);
			server.shutdownOutput();

			ObjectInputStream ois = new ObjectInputStream(is);
			message = (CommMessage<T>) ois.readObject();
			os.close();
			is.close();
			server.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return message.getResponse();
	}

	@Override
	public byte[] retrieveData(RequestProcessingResultMessage message, Node node) {
		byte[] retrievedData = null;

		try {
			Socket server = new Socket(node.getIpaddress(), node.getPort());
			OutputStream os = server.getOutputStream();
			InputStream is = server.getInputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.flush();

			oos.writeObject(message);
			oos.flush();

			ObjectInputStream ois = new ObjectInputStream(is);
			RequestProcessingResultMessage returnedMessage = (RequestProcessingResultMessage) ois
					.readObject();
			ReturnResultPayload returnedPayload = returnedMessage.getResponse();
			retrievedData = returnedPayload.getResponseValue();

			oos.close();
			os.close();
			ois.close();
			is.close();
			server.close();
		} catch (UnknownHostException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return retrievedData;
	}

	public void setClient(Socket client) {
		this.client = client;
	}

}
