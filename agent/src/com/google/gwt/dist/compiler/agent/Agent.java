package com.google.gwt.dist.compiler.agent;

import java.net.ServerSocket;
import java.net.Socket;

public class Agent extends Thread {

	private ServerSocket dataServer;

	public static void main(String argv[]) throws Exception {
		new Agent();
	}
	
	public Agent() throws Exception {
		dataServer = new ServerSocket(3000);
	     System.out.println("Server listening on port 3000.");
	     this.start();
	   } 

	public void run() {
		while (true) {
			try {
				System.out.println("Waiting for connections.");
				Socket client = dataServer.accept();
				System.out.println("Accepted a connection from: "
						+ client.getInetAddress());
				@SuppressWarnings("unused")
				Connect c = new Connect(client);
			} catch (Exception e) {
			}
		}
	}

}
