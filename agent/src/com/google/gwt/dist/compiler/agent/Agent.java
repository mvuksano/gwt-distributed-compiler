package com.google.gwt.dist.compiler.agent;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.google.gwt.dist.compiler.agent.impl.DataProcessorMock;

public class Agent extends Thread {

	private SessionManager sessionManager;

	public static void main(String argv[]) throws Exception {
		ApplicationContext appContext = new FileSystemXmlApplicationContext(
				new File("config/applicationContext.xml").toString());
		SessionManager sm = (SessionManager) appContext
				.getBean("sessionManager");

		new Agent(sm);
	}

	public Agent(SessionManager sessionManager) throws Exception {
		this.sessionManager = sessionManager;
		this.start();
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		DataProcessorMock dpm = new DataProcessorMock();
		executorService.execute(dpm);
		executorService.shutdown();
	}

	public void run() {
		while (true) {
			try {
				sessionManager.startListening();
				sessionManager.stopListening();
			} catch (Exception e) {
			}
		}
	}

}
