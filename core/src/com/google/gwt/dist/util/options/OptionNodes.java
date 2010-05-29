package com.google.gwt.dist.util.options;

import java.util.List;

import com.google.gwt.dist.Node;

/**
 * Specifies which nodes to use for compilation.
 */
public interface OptionNodes {

	List<Node> getNodes();
	
	void setNodes(List<Node> nodes);
	
}
