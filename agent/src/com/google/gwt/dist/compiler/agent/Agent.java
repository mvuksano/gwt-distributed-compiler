package com.google.gwt.dist.compiler.agent;

import java.net.ServerSocket;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Agent extends Thread {

	private ServerSocket server;
	private static final Logger logger = Logger.getLogger(Agent.class);

	public static void main(String argv[]) throws Exception {
		new Agent();
	}

	public Agent() throws Exception {

		server = new ServerSocket(3000);

		ApplicationContext appContext = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		while (true) {
			ClientWorker worker = (ClientWorker) appContext
					.getBean("clientWorker");
			Thread t = new Thread(worker);

			worker.setClient(server.accept());

			t.start();
		}
	}
}
