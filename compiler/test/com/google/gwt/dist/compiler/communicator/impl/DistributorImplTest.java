package com.google.gwt.dist.compiler.communicator.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gwt.dev.CompilePerms.CompilePermsOptions;
import com.google.gwt.dist.Node;
import com.google.gwt.dist.compiler.communicator.Distributor;

public class DistributorImplTest {

	@BeforeClass
	public void setUp() {
	}

	@Test
	public void testDistribute() {
		CompilePermsOptions options = mock(CompilePermsOptions.class);
		when(options.getPermsToCompile()).thenReturn(
				new int[] { 0, 1, 2, 3, 4, 5 });
		
		Node node1 = mock(Node.class);
		Node node2 = mock(Node.class);
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(node1);
		nodes.add(node2);
		
		Distributor distributor = new DistributorImpl();
		Map<Node, int[]> distributed = distributor.distribute(options, nodes);
		
		Assert.assertEquals(distributed.get(node1).length, 3);
		Assert.assertEquals(distributed.get(node2).length, 3);

	}

}
