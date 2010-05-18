package com.google.gwt.dist.compiler.agent.events;

import com.google.gwt.dist.comm.SendDataPayload;

/**
 * All objects that want to be notified of data received event should implement
 * this interface.
 */
public interface DataReceivedListener {
	void onDataReceived(SendDataPayload receivedData);
}
