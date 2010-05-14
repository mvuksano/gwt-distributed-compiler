package com.google.gwt.dist.compiler.agent;

import java.net.Socket;

import org.springframework.context.ApplicationContext;

public class ClientWorker implements Runnable {

	private Socket client;
	private SessionManager sessionManager;

	public ClientWorker(Socket client, ApplicationContext appContext) {
		this.client = client;
		sessionManager = (SessionManager) appContext.getBean("sessionManager");
	}

	@Override
	public void run() {
		this.sessionManager.processConnection(client);

	}
}
