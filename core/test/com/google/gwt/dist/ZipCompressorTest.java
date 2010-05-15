package com.google.gwt.dist;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gwt.dist.util.Util;
import com.google.gwt.dist.util.ZipCompressor;

public class ZipCompressorTest {

	File dirToCompress;
	ZipCompressor zipCompressor;

	@BeforeClass
	public void setUp() {
		File source = new File(System.getProperty("user.dir")).getParentFile();
		dirToCompress = new File(
				source.toString()
						+ "\\core\\test\\com\\google\\gwt\\dist\\resources\\sample-to-be-compressed\\");
		zipCompressor = new ZipCompressor();
	}

	@Test
	public void testArchiveAndCompress() throws IOException {
		byte[] compressedOutput;
		String[] expectedZipEntryNames = {
				"permutation-0.js",
				"test-folder" + Util.getFolderSeparatorInZipArchive()
						+ "permutation-1.js" };
		compressedOutput = zipCompressor.archiveAndCompressDir(dirToCompress,
				Pattern.compile("permutation-[0-9+].js")).toByteArray();
		ByteArrayInputStream is = new ByteArrayInputStream(compressedOutput);
		CheckedInputStream checksum = new CheckedInputStream(is, new Adler32());
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(
				checksum));
		ZipEntry zipEntry;
		int counter = 0;
		while ((zipEntry = zis.getNextEntry()) != null) {
			Assert.assertEquals(zipEntry.getName(),
					expectedZipEntryNames[counter++]);
		}
	}

	public void testMergeZipOutputStreams() throws IOException {
		ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
		ZipOutputStream z1 = new ZipOutputStream(baos1);

		ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
		ZipOutputStream z2 = new ZipOutputStream(baos2);

		String[] expectedZipEntryNames = { "test-folder1",
				"test-folder1/test-file1", "test-folder1/test-file2",
				"test-folder2", "test-folder2/test-file1",
				"test-folder2/test-file2" };

		ZipInputStream result = zipCompressor.mergeOutputStreams(z1, z2);
		
		ZipEntry zipEntry;
		int counter = 0;
		while ((zipEntry = result.getNextEntry()) != null) {
			Assert.assertEquals(zipEntry.getName(),
					expectedZipEntryNames[counter++]);
		}
	}
}
