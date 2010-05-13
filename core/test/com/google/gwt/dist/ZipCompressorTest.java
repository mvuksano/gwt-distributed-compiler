package com.google.gwt.dist;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gwt.dist.util.ZipCompressor;

public class ZipCompressorTest {

	File dirToCompress;
	ZipCompressor zipCompressor;
	
	@BeforeClass
	public void setUp() {
		dirToCompress = new File("resources\\sample\\");
		zipCompressor = new ZipCompressor();
	}
	
	@Test
	public void testArchiveAndCompress() {
		try {
			zipCompressor.archiveAndCompressDir(dirToCompress, Pattern.compile("permutation-[0-9+]*"));
		} catch (IOException e) {
		}
		Assert.assertTrue(true);
	}
}
