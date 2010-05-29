package com.google.gwt.dist.util.options;

import java.io.File;

/**
 * Specifies folder in which lib files used by client side are stored.
 */
public interface OptionClassPath {
	
	File getGwtClassPath();
	
	void setGwtClassPath(File classpath);

}
