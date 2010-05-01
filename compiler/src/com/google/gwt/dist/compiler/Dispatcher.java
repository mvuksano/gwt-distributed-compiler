package com.google.gwt.dist.compiler;

import java.io.OutputStream;

import com.google.gwt.dist.Node;

/**
 * Any dispatcher service must implement this interface.
 */
public interface Dispatcher {
	public void dispatchData(OutputStream data, Node... nodes);
}
