package com.google.gwt.dist.compiler.communicator.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.dist.Node;
import com.google.gwt.dist.compiler.communicator.Distributor;

/**
 * Distributor implementation. This is the most simple distribution
 * implementation. Permutations are distributed evenly to each node.
 */
public class DistributorImpl implements Distributor {

	/**
	 * Distributes permutations across supplied nodes
	 * 
	 * @param options
	 *            CompilerOptions to redistribute.
	 * @param nodes
	 *            Nodes across which to distribute.
	 * @return Map which maps an array of permutations to a node.
	 */
	public Map<Node, int[]> distribute(int[] permsToDistribute, List<Node> nodes) {
		Multimap<Node, Integer> map = HashMultimap.create();
		for (int i = 0; i < permsToDistribute.length; i++) {
			int temp = i % nodes.size();
			map.put(nodes.get(temp), Integer.valueOf(i));
		}
		
		Map<Node, int[]> result = new HashMap<Node, int[]>();
		for (Node n : nodes) {
			result.put(n, convertToIntArray(map.get(n)));
		}
		return result;
	}

	private int[] convertToIntArray(Collection<Integer> collection) {
		int[] result = new int[collection.size()];
		Integer[] temp = collection.toArray(new Integer[0]);
		for (int i = 0 ; i < temp.length; i++) {
			result[i] = temp[i];
		}
		return result;
	}
}
