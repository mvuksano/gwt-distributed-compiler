package com.google.gwt.dist;

import java.io.File;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests related to reading agents' settings from a file.
 * 
 */
public class AgentsSettingsTest {

	private ApplicationContext context;
	private Application app;
	
	private static String APPLICATION_CONTEXT_FILE_LOCATION = "test/com/google/gwt/dist/resources/applicationContext.xml";
	private static String APPLICATION_SETTINGS = "test/com/google/gwt/dist/resources/testConfig.xml";

	@BeforeClass
	public void init() {
		context = new FileSystemXmlApplicationContext(new File(
				APPLICATION_CONTEXT_FILE_LOCATION).toString());
		app = context.getBean(Application.class);
	}
	
	@Test
	public void validValues() {
		app.loadSettings(new FileSystemResource(APPLICATION_SETTINGS));
		AgentsSettings settings = app.getSettings();
		Assert.assertEquals(settings.getNodes().size(), 2);
		
		Node node = settings.getNodes().get(0);
		Assert.assertEquals(node.getIpaddress(), "localhost");
		Assert.assertEquals(node.getPort(), 3000);
	}

}
