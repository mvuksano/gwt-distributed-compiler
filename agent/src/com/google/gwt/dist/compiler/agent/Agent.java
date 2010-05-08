package com.google.gwt.dist.compiler.agent;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Agent extends Thread {

	private ServerSocket dataServer;
	private SessionManager sessionManager;

	public static void main(String argv[]) throws Exception {
		ApplicationContext appContext = new FileSystemXmlApplicationContext(
				new File("config/applicationContext.xml").toString());
		new Agent((SessionManager) appContext
				.getBean("sessionManager"));
	}

	public Agent(SessionManager sessionManager) throws Exception {
		this.sessionManager = sessionManager;
		this.start();
	}

	public void run() {
		while (true) {
			try {
				sessionManager.startListening();
			} catch (Exception e) {
			}
		}
	}

}
