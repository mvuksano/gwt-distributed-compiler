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
import com.google.gwt.dist.comm.CommMessageResponse;
import com.google.gwt.dist.comm.ProcessingStateResponse;
import com.google.gwt.dist.compiler.communicator.Communicator;

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
	public <T extends CommMessageResponse> T sendMessage(CommMessage<T> message, Node node) {
		try {
			Socket server = new Socket(node.getIpaddress(), node.getPort());
			InputStream is = server.getInputStream();
			OutputStream os = server.getOutputStream();

			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(message);
			server.shutdownOutput();

			ObjectInputStream ois = new ObjectInputStream(is);
			message = (CommMessage<T>) ois.readObject();
			System.out
					.println(((ProcessingStateResponse) message.getResponse())
							.getCurrentState());
			os.close();
			is.close();
			server.close();
		} catch (UnknownHostException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return message.getResponse();
	}
	
	public void setClient(Socket client) {
		this.client = client;
	}

}
