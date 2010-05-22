package com.google.gwt.dist;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	SessionManager sessionManager1;
	SessionManager sessionManager2;

	@BeforeClass
	public void setUp() {
		app = new Application(mock(Communicator.class),
				mock(ZipCompressor.class), mock(ZipDecompressor.class), mock(Distributor.class),
				mock(TreeLogger.class));
		
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
		Assert.assertFalse(app.allSessionManagersFinished(sessionManagers, sessionManagerStatusList));
		
		sessionManagerStatusList.put(sessionManager1, Boolean.valueOf(true));
		Assert.assertFalse(app.allSessionManagersFinished(sessionManagers, sessionManagerStatusList));
		
		sessionManagerStatusList.put(sessionManager2, Boolean.valueOf(true));
		Assert.assertTrue(app.allSessionManagersFinished(sessionManagers, sessionManagerStatusList));
	}

}
