package com.google.gwt.dist.compiler.agent;

import java.net.Socket;

public class ClientWorker implements Runnable {

	private Socket client;
	private SessionManager sessionManager;

	public ClientWorker() {
	}

	@Override
	public void run() {
		this.sessionManager.processConnection(client);

	}
	
	public SessionManager getSessionManager() {
		return this.sessionManager;
	}
	
	public void setClient(Socket client) {
		this.client = client;
	}
	
	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
}
