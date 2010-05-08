package com.google.gwt.dist;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gwt.dist.SessionState.State;

public class SessionStateTest {
	
	@BeforeClass
	public void init() {
		
	}
	
	@Test
	public void testSwitching() {
		SessionState state = new SessionState();
		Assert.assertEquals(state.getState(), State.READY);
		
		state.switchState();
		Assert.assertEquals(state.getState(), State.INPROGRESS);
		
		state.switchState();
		Assert.assertEquals(state.getState(), State.COMPLETED);
	}
}
