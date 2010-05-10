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

	private static final Logger logger = Logger
			.getLogger(CommunicatorImpl.class.getName());

	@Override
	public void sendData(InputStream inputStream, Node node) {
	}

	@Override
	public CommMessageResponse sendMessage(CommMessage message, Node node) {
		try {
			Socket server = new Socket(node.getIpaddress(), node.getPort());
			InputStream is = server.getInputStream();
			OutputStream os = server.getOutputStream();

			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(message);
			server.shutdownOutput();

			ObjectInputStream ois = new ObjectInputStream(is);
			message = (CommMessage) ois.readObject();
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

}
