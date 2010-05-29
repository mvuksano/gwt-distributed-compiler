package com.google.gwt.dist.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {

	@SuppressWarnings("unchecked")
	private static final Class[] parameters = new Class[] { URL.class };
	private static final Logger logger = Logger.getLogger(Util.class.getName());

	/**
	 * This method adds a folder to class search path at runtime.
	 * 
	 * @param f
	 *            Folder to be added.
	 */
	public static void addFolderToClasspath(File f, ClassLoader classLoader) {
		URL uncompressedSrcURL = null;
		try {
			uncompressedSrcURL = new URL("file://" + f.getAbsolutePath()
					+ File.separator + "*");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		URL elements[] = { uncompressedSrcURL };
		new URLClassLoader(elements, classLoader);
	}

	public static void addUrl(URL u) {
		URLClassLoader sysloader = (URLClassLoader) ClassLoader
				.getSystemClassLoader();
		Class<URLClassLoader> sysclass = URLClassLoader.class;

		try {
			Method method = sysclass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			method.invoke(sysloader, new Object[] { u });
		} catch (NoSuchMethodException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} catch (InvocationTargetException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} catch (IllegalAccessException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	public static String getFolderSeparatorInZipArchive() {
		return "/";
	}
	

	/**
	 * Get MD5, which is stored as a byte array, as a String representation.
	 * 
	 * @param md5 byte array to be converted into a string.
	 * @return String representation of the MD5.
	 */
	public static String getMD5AsHex(byte[] md5) {
		String result = "";
		for (int i = 0; i < md5.length; i++) {
			result += Integer.toString((md5[i] & 0xff) + 0x100, 16)
					.substring(1);
		}
		return result;
	}

	/**
	 * Converts array of bytes into object.
	 * 
	 * @param bytes
	 *            Bytes to be converted to object.
	 * @return Object read from byte array or null if conversion failed.
	 */
	public static Object byteArrayToObject(byte[] bytes) {
		Object obj = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			obj = ois.readObject();
			return obj;
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		}
		return null;
	}

	public static byte[] objectToByteArray(Object o) {
		byte[] data = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(o);
			oos.flush();
			oos.close();
			bos.close();
			data = bos.toByteArray();
		} catch (IOException e) {
		}
		return data;
	}
}
