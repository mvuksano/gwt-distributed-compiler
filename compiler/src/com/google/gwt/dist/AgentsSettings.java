package com.google.gwt.dist;

import java.util.List;

/**
 * This class is used to manipulate information about available Agents.
 * 
 */
public class AgentsSettings {

	private List<Node> nodes;
	private String uuid;

	public List<Node> getNodes() {
		return nodes;
	}
	
	public String getUUID() {
		return uuid;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}
	
	public void setUUID(String uuid) {
		this.uuid = uuid;
	}
}
