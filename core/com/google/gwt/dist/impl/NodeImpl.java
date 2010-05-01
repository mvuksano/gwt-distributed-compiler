package com.google.gwt.dist.impl;

import java.net.InetAddress;

import com.google.gwt.dist.Node;

/**
 * Concrete implementation of node
 */
public class NodeImpl implements Node {
	private InetAddress address;
	private int port;

	public NodeImpl(InetAddress ipAddress, int port) {
		this.address = ipAddress;
		this.port = port;
	}

	public InetAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}
}
