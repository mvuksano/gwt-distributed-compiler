package com.google.gwt.dist.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Used to archive and compress folders using standard zip algorithm.
 */
public class ZipCompressor {

	static final int BUFFER = 2048;

	public ByteArrayOutputStream archiveAndCompressDir(File directory)
			throws IOException {

		ByteArrayOutputStream destination = new ByteArrayOutputStream();
		CheckedOutputStream checksum = new CheckedOutputStream(destination,
				new Adler32());
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
				checksum));
		out.setMethod(ZipOutputStream.DEFLATED);

		addFilesToPackage(directory, "", out);

		out.close();

		return destination;
	}

	private void addFilesToPackage(File dir, String pathPrefix,
			ZipOutputStream out) throws IOException {
		File[] list = dir.listFiles();

		for (int k = 0; k < list.length; k++) {
			File f = list[k];
			if (f.isDirectory()) {
				addFilesToPackage(f, pathPrefix + f.getName() + "/", out);
			} else {
				ZipEntry ze = new ZipEntry(pathPrefix + f.getName());
				out.putNextEntry(ze);
				InputStream in = new FileInputStream(f);
				byte[] buff = new byte[2056];
				int bytesRead = 0;
				while ((bytesRead = in.read(buff)) > -1) {
					out.write(buff, 0, bytesRead);
				}
				out.closeEntry();
				in.close();
			}
		}
	}
}
