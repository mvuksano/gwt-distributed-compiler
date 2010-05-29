package com.google.gwt.dist;

import java.io.File;
import java.util.Map;

/**
 * This interface defines methods for checking existing data on the agent.
 */
public interface DataChecker {

	/**
	 * Get a map that holds key value pairs where key is file name and value is its MD5.
	 * 
	 * @param directory
	 * @return
	 */
	Map<String, byte[]> getMD5Matrix(File directory);
}
