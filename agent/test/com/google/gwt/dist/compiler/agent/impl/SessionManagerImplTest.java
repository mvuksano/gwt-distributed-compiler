package com.google.gwt.dist.compiler.agent.impl;

import java.io.File;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gwt.dist.compiler.agent.SessionManager;

/**
 * Tests related to SessionManagerImpl.
 */
public class SessionManagerImplTest {

	SessionManager sessionManager;
	
	@BeforeClass
	public void init() {
		ApplicationContext appContext = new FileSystemXmlApplicationContext(
				new File("config/applicationContext.xml").toString());
		sessionManager = (SessionManager) appContext
				.getBean("sessionManager");
	}
	
	@Test
	public void compilePermsFinished() {
		Assert.assertNotNull(((SessionManagerImpl)sessionManager).getCommunicator());
	}
	
	@Test
	public void compilePermsNotFinished() {
		Assert.fail();
	}
}
