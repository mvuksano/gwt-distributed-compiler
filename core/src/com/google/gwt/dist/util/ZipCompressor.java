package com.google.gwt.dist.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Used to archive and compress folders using standard zip algorithm.
 */
public class ZipCompressor {

	private Pattern excludeDirPattern;

	static final int BUFFER = 2048;
	static final Logger logger = Logger
			.getLogger(ZipCompressor.class.getName());

	public ByteArrayOutputStream archiveAndCompressDir(File directory) {
		return archiveAndCompressDir(directory, false);
	}

	/**
	 * Method compresses files in the directory.
	 * 
	 * @param directory
	 *            Directory to compress.
	 * @param preserveParentFolderName
	 *            true if folder which is being compressed should be parent in
	 *            the archive also.
	 * @return Compressed output stream as byte array.
	 */
	public ByteArrayOutputStream archiveAndCompressDir(File directory,
			boolean preserveParentFolderName) {
		ByteArrayOutputStream destination = new ByteArrayOutputStream();
		CheckedOutputStream checksum = new CheckedOutputStream(destination,
				new Adler32());
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
				checksum));
		out.setMethod(ZipOutputStream.DEFLATED);

		try {
			if (preserveParentFolderName) {
				addFilesToPackage(directory, directory.getName()
						+ Util.getFolderSeparatorInZipArchive(), out, null);
			} else {
				addFilesToPackage(directory, "", out, null);
			}
			out.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE,
					"There was a problem while adding files into Zip stream.");
		}

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
	 * @param includeFilePattern
	 *            Regular expression which acts as a filter. All the files that
	 *            do not match that regular expression are not added to the
	 *            stream.
	 * @param out
	 *            Output stream to which data is written.
	 * @throws IOException
	 */
	private void addFilesToPackage(File dir, String pathPrefix,
			ZipOutputStream out, Pattern includeFilePattern) throws IOException {
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
						includeFilePattern);
			} else {
				if (includeFilePattern != null
						&& !includeFilePattern.matcher(f.getName()).matches()) {
					continue;
				}
				ZipEntry ze = new ZipEntry(pathPrefix + f.getName());
				out.putNextEntry(ze);
				InputStream in = new FileInputStream(f);
				byte[] buff = new byte[2048];
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

	public ZipInputStream mergeZippedStreams(ZipInputStream... z) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CheckedOutputStream checksum = new CheckedOutputStream(baos,
				new Adler32());
		ZipOutputStream mergedStream = new ZipOutputStream(checksum);
		mergedStream.setMethod(ZipOutputStream.DEFLATED);
		try {
			for (ZipInputStream zis : z) {
				ZipEntry zipEntry = null;
				while ((zipEntry = zis.getNextEntry()) != null) {
					mergedStream.putNextEntry(zipEntry);
					byte[] buff = new byte[2048];
					int bytesRead = 0;
					while ((bytesRead = zis.read(buff)) > -1) {
						mergedStream.write(buff, 0, bytesRead);
					}
					mergedStream.flush();
					mergedStream.closeEntry();
					zis.closeEntry();
				}
			}
			mergedStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		ZipInputStream zisNew = new ZipInputStream(bais);

		return zisNew;
	}

	public void setExcludePattern(Pattern excludePattern) {
		this.excludeDirPattern = excludePattern;
	}
}