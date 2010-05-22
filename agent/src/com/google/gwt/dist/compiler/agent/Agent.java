package com.google.gwt.dist.compiler.agent;

import java.io.File;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.google.gwt.dist.compiler.agent.processor.DataProcessor;

public class Agent extends Thread {

	private ServerSocket server;
	private static final Logger logger = Logger.getLogger(Agent.class);

	public static void main(String argv[]) throws Exception {
		new Agent();
	}

	public Agent() throws Exception {
		
		server = new ServerSocket(3000);
		
		ApplicationContext appContext = new FileSystemXmlApplicationContext(
				new File("config/applicationContext.xml").toString());
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		logger.debug("Started " + executorService.getClass().getName());
		DataProcessor dp = (DataProcessor) appContext.getBean("dataProcessor");
		executorService.execute(dp);
		executorService.shutdown();		
		while (true) {
			ClientWorker worker = (ClientWorker) appContext.getBean("clientWorker");
			Thread t = new Thread(worker);
			
			worker.setClient(server.accept());
			
			t.start();
		}
	}
}
