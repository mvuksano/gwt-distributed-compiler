package com.google.gwt.dist.compiler.communicator;

import java.util.List;
import java.util.Map;

import com.google.gwt.dist.Node;

public interface Distributor {
	
	public Map<Node, int[]> distribute(int[] permsToDistributor, List<Node> nodes);

}
