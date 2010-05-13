package com.google.gwt.dist.compiler.agent;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.google.gwt.dist.compiler.agent.impl.DataProcessorImpl;

public class Agent extends Thread {

	private SessionManager sessionManager;
	private static final Logger logger = Logger.getLogger(Agent.class.getName());

	public static void main(String argv[]) throws Exception {
		ApplicationContext appContext = new FileSystemXmlApplicationContext(
				new File("config/applicationContext.xml").toString());
		SessionManager sm = (SessionManager) appContext
				.getBean("sessionManager");

		new Agent(sm);
	}

	public Agent(SessionManager sessionManager) throws Exception {
		ApplicationContext appContext = new FileSystemXmlApplicationContext(
				new File("config/applicationContext.xml").toString());
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		//DataProcessor dp = new DataProcessorMock(15000);
		DataProcessor dp = (DataProcessorImpl) appContext.getBean("dataprocessor");
		dp.addListener(this.sessionManager);
		executorService.execute(dp);
		executorService.shutdown();
		this.sessionManager = sessionManager;
		this.start();
	}

	public void run() {
		while (true) {
			try {
				sessionManager.startListening();
				sessionManager.stopListening();
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
	}

}
