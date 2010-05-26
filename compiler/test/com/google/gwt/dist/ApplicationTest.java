package com.google.gwt.dist;

import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dist.compiler.communicator.Communicator;
import com.google.gwt.dist.compiler.communicator.Distributor;
import com.google.gwt.dist.util.ZipCompressor;
import com.google.gwt.dist.util.ZipDecompressor;

public class ApplicationTest {

	Application app;
	ApplicationContext appContext;
	SessionManager sessionManager1;
	SessionManager sessionManager2;

	private static String APPLICATION_CONTEXT_FILE_LOCATION = "test/com/google/gwt/dist/resources/applicationContext-test.xml";
	private static String APPLICATION_SETTINGS_NO_UUID = "test/com/google/gwt/dist/resources/testConfig-no-uuid.xml";
	private static String APPLICATION_SETTINGS_WITH_UUID = "test/com/google/gwt/dist/resources/testConfig-with-uuid.xml";
	private static String APPLICATION_SETTINGS_TEMP = "test/com/google/gwt/dist/resources/testConfig-temp.xml";

	@BeforeClass
	public void setUp() {
		app = new Application(mock(Communicator.class),
				mock(ZipCompressor.class), mock(ZipDecompressor.class),
				mock(Distributor.class), mock(TreeLogger.class));

		sessionManager1 = mock(SessionManager.class);
		sessionManager2 = mock(SessionManager.class);
	}

	@Test
	public void testAllSessionManagersFinished() {
		List<SessionManager> sessionManagers = new ArrayList<SessionManager>();
		sessionManagers.add(sessionManager1);
		sessionManagers.add(sessionManager2);
		Map<SessionManager, Boolean> sessionManagerStatusList = new HashMap<SessionManager, Boolean>();
		sessionManagerStatusList.put(sessionManager1, Boolean.valueOf(false));
		sessionManagerStatusList.put(sessionManager2, Boolean.valueOf(false));
		Assert.assertFalse(app.allSessionManagersFinished(sessionManagers,
				sessionManagerStatusList));

		sessionManagerStatusList.put(sessionManager1, Boolean.valueOf(true));
		Assert.assertFalse(app.allSessionManagersFinished(sessionManagers,
				sessionManagerStatusList));

		sessionManagerStatusList.put(sessionManager2, Boolean.valueOf(true));
		Assert.assertTrue(app.allSessionManagersFinished(sessionManagers,
				sessionManagerStatusList));
	}

	/**
	 * This test verifies that a UUID is generated if one is not specified in
	 * the config file.
	 */
	@Test
	public void testGeneratingUUID() {
		appContext = new FileSystemXmlApplicationContext(
				APPLICATION_CONTEXT_FILE_LOCATION);
		app = (Application) appContext.getBean("application");
		Resource inputResource = new FileSystemResource(APPLICATION_SETTINGS_NO_UUID);
		app.loadSettings(inputResource);
		File temp = new File(APPLICATION_SETTINGS_TEMP);
		try {
			temp.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Resource outputResource = new FileSystemResource(temp);
		Assert.assertFalse(app.validUUID(app.getSettings()));
		app.saveSettings(outputResource);

		app.setSettings(null);
		Assert.assertNull(app.getSettings());

		app.loadSettings(outputResource);
		Assert.assertTrue(!(app.getSettings().getUUID().equals("")));
		temp.delete();
	}

	/**
	 * This test verifies that existing UUID is reused, if it exists.
	 */
	@Test
	public void testUsingExistingUUID() {
		appContext = new FileSystemXmlApplicationContext(
				APPLICATION_CONTEXT_FILE_LOCATION);
		app = (Application) appContext.getBean("application");
		Resource inputResource = new FileSystemResource(APPLICATION_SETTINGS_WITH_UUID);
		app.loadSettings(inputResource);
		Assert.assertTrue(app.getSettings().getUUID().equals("de6a4ca0-950a-4d11-8085-dd9354a8b512"));
		Assert.assertTrue(app.validUUID(app.getSettings()));
	}

}
