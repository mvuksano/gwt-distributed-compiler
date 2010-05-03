package com.google.gwt.dist.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Used to archive and compress folders using standard zip algorithm.
 */
public class ZipCompressor {
	
	private Pattern excludePattern;

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

	/**
	 * Add files found in a directory to output stream, preserving path
	 * @param dir Directory to add to output stream.
	 * @param pathPrefix Prefix to use for path (preserves folder structure)
	 * @param out Output stream to which data is written.
	 * @throws IOException
	 */
	private void addFilesToPackage(File dir, String pathPrefix,
			ZipOutputStream out) throws IOException {
		File[] list = dir.listFiles();
		List<File> filteredList = new ArrayList<File>();
		
		if (getExcludePattern() != null) {
			for (File f : list) {
				Matcher m = excludePattern.matcher(f.getName());
				if (!m.matches()) {
					filteredList.add(f);
				}
			}
		}

		for (File f : filteredList) {
			if (f.getName().startsWith(".")) {
				continue;
			}
			if (f.isDirectory()) {
				addFilesToPackage(f, pathPrefix + f.getName() + File.separator, out);
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
	
	public Pattern getExcludePattern() {
		return this.excludePattern;
	}
	
	public void setExcludePattern(Pattern excludePattern) {
		this.excludePattern = excludePattern;
	}
}
