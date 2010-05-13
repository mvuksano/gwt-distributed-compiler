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

	private Pattern excludeDirPattern;

	static final int BUFFER = 2048;

	public ByteArrayOutputStream archiveAndCompressDir(File directory)
			throws IOException {

		ByteArrayOutputStream destination = new ByteArrayOutputStream();
		CheckedOutputStream checksum = new CheckedOutputStream(destination,
				new Adler32());
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
				checksum));
		out.setMethod(ZipOutputStream.DEFLATED);

		addFilesToPackage(directory, "", out, null);

		out.close();

		return destination;
	}

	/**
	 * Archives and Compresses the Directory, but includes only files that match
	 * the regular expression pattern.
	 * 
	 * @param directory
	 * @param fileFilter
	 * @return
	 * @throws IOException
	 */
	public ByteArrayOutputStream archiveAndCompressDir(File directory,
			Pattern fileFilter) throws IOException {

		ByteArrayOutputStream destination = new ByteArrayOutputStream();
		CheckedOutputStream checksum = new CheckedOutputStream(destination,
				new Adler32());
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
				checksum));
		out.setMethod(ZipOutputStream.DEFLATED);

		addFilesToPackage(directory, "", out, fileFilter);

		out.close();

		return destination;
	}

	/**
	 * Add files found in a directory to output stream, preserving path
	 * 
	 * @param dir
	 *            Directory to add to output stream.
	 * @param pathPrefix
	 *            Prefix to use for path (preserves folder structure).
	 * @param regexpPattern
	 *            Regular expression which acts as a filter. All the files that
	 *            do not match that regular expression are not added to the
	 *            stream.
	 * @param out
	 *            Output stream to which data is written.
	 * @throws IOException
	 */
	private void addFilesToPackage(File dir, String pathPrefix,
			ZipOutputStream out, Pattern regexpPattern) throws IOException {
		File[] list = dir.listFiles();
		List<File> filteredList = new ArrayList<File>();

		if (getExcludePattern() != null) {
			for (File f : list) {
				Matcher m = excludeDirPattern.matcher(f.getName());
				if (!m.matches()) {
					filteredList.add(f);
				}
			}
		} else {
			for (File f : list) {
				filteredList.add(f);
			}
		}

		for (File f : filteredList) {
			if (f.getName().startsWith(".")) {
				continue;
			}
			if (f.isDirectory()) {
				addFilesToPackage(f, pathPrefix + f.getName()
						+ Util.getFolderSeparatorInZipArchive(), out,
						regexpPattern);
			} else {
				if (regexpPattern != null
						&& !regexpPattern.matcher(f.getName()).matches()) {
					continue;
				}
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
		return this.excludeDirPattern;
	}

	public void setExcludePattern(Pattern excludePattern) {
		this.excludeDirPattern = excludePattern;
	}
}