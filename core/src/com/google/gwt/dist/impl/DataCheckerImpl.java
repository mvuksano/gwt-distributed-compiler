package com.google.gwt.dist.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gwt.dist.DataChecker;

public class DataCheckerImpl implements DataChecker {
	
	private static final Logger logger = Logger.getLogger(DataCheckerImpl.class);

	@Override
	public Map<String, byte[]> getMD5Matrix(File directory) {
		Map<String, byte[]> map = new HashMap<String, byte[]>();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			for (File f : directory.listFiles()) {
				InputStream is = new FileInputStream(f);
				BufferedInputStream bis = new BufferedInputStream(is);
				byte[] buffer = new byte[8192];
				int read = 0;
				while ((read = bis.read(buffer)) > 0) {
					md.update(buffer, 0, read);
				}
				map.put(f.getName(), md.digest());
			}
		} catch (NoSuchAlgorithmException e) {
			logger.error("" + e.getMessage());
		} catch (IOException e) {
			logger.error("" + e.getMessage());
		}
		return map;
	}
}
