package com.google.gwt.dist.util.options;

/**
 * Specifies which universally unique identifier to use. This is very important
 * for the remote agent as it uses this number to know if some store code
 * belongs to this instance of the compiler or some other.
 */
public interface OptionUUID {

	String getUUID();

	void setUUID(String uuid);
}
