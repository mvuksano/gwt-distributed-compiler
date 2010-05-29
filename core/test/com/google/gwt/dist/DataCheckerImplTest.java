package com.google.gwt.dist;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gwt.dist.impl.DataCheckerImpl;
import com.google.gwt.dist.util.Util;

public class DataCheckerImplTest {

	private DataChecker dataChecker;
	private File directory;

	@BeforeClass
	public void setUp() {
		File source = new File(System.getProperty("user.dir")).getParentFile();
		this.directory = new File(
				source.toString()
						+ "\\core\\test\\com\\google\\gwt\\dist\\resources\\sample-to-be-compressed\\test-folder1\\");
		this.dataChecker = new DataCheckerImpl();
	}

	@Test
	public void testGetMD5Matrix() throws NoSuchAlgorithmException, IOException {
		Map<String, byte[]> map = null;
		Map<String, String> expectedMap = new HashMap<String, String>();

		expectedMap.put("test-file1.txt", "e3db3275132905faa35fdbf57e4d36e8");
		expectedMap.put("test-file2.txt", "676f7876d5a063a19eea87b015e562ec");

		map = this.dataChecker.getMD5Matrix(directory);
		for (String s : map.keySet()) {
			Assert.assertEquals(Util.getMD5AsHex(map.get(s)), expectedMap.get(s));
		}
	}

}
