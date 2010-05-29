package com.google.gwt.dist.util.arg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.dist.Node;
import com.google.gwt.dist.impl.NodeImpl;
import com.google.gwt.dist.util.options.OptionNodes;
import com.google.gwt.util.tools.ArgHandler;

/**
 * Argument handler for nodes parameter.
 */
public class ArgHandlerNodes extends ArgHandler {

	private final OptionNodes option;

	public ArgHandlerNodes(OptionNodes option) {
		this.option = option;
	}

	@Override
	public String getPurpose() {
		return "Specify nodes to be used for compilation";
	}

	@Override
	public String getTag() {
		return "-nodes";
	}

	@Override
	public String[] getTagArgs() {
		return new String[] { "nodes" };
	}

	@Override
	public int handle(String[] args, int startIndex) {
		List<Node> nodes = new ArrayList<Node>();
		for (String s :	Arrays.asList(args[startIndex + 1].split(";"))) {
			String[] temp = s.split(":");
			nodes.add(new NodeImpl(temp[0], Integer.parseInt(temp[1])));
		}
		option.setNodes(nodes);
		return 1;
	}
	
	  @Override
	  public boolean isRequired() {
	    return true;
	  }

}
