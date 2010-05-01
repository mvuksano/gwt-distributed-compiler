package com.google.gwt.dist;

import java.net.InetAddress;

/**
 * A node where an agent is running.
 */
public interface Node {
	InetAddress getAddress();

	int getPort();
}
