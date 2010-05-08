package com.google.gwt.dist.impl;

import com.google.gwt.dist.Node;

/**
 * Concrete implementation of node
 */
public class NodeImpl implements Node {
	private String ipaddress;
	private int port;
	
	public NodeImpl() {
	}

	public NodeImpl(String ipaddress, int port) {
		this.ipaddress = ipaddress;
		this.port = port;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public int getPort() {
		return port;
	}
	
	public void setIpaddress(String address) {
		this.ipaddress = address;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
}
