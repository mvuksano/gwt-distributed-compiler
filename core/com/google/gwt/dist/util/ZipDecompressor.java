package com.google.gwt.dist.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Helper class that implements decompression of a stream which has been
 * previously compressed using standard zip algorithm.
 */
public class ZipDecompressor {

	private Logger logger = Logger.getLogger(ZipCompressor.class.getName());

	private static final int BUFFER = 2048;

	/**
	 * Uncompress a stream into a directory.
	 * 
	 * @param stream
	 *            ByteArrayOutputStream to be uncompressed.
	 * @param directory
	 *            The directory into which to store the uncompressed stream.
	 */
	public void decompressAndStoreToFile(ByteArrayOutputStream stream,
			File directory) {
		try {
			directory.mkdir();
			BufferedOutputStream dest = null;
			FileInputStream fis = new FileInputStream("helper.zip");
			CheckedInputStream checksum = new CheckedInputStream(fis,
					new Adler32());
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(
					checksum));
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				File destFile = new File(directory, entry.getName());
				destFile = new File(destFile.getParent());
				destFile.mkdirs();

				System.out.println("Extracting: " + entry);
				int count;
				byte data[] = new byte[BUFFER];
				// write the files to the disk
				File f = new File(directory.getAbsolutePath() + File.separator + entry.getName());
				f.createNewFile();
				FileOutputStream fos = new FileOutputStream(f);
				dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = zis.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
			}
			zis.close();
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "File not found!");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Problem writing to a file or directory.");
		}

	}
}
