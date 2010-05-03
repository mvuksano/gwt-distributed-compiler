package com.google.gwt.dist.compiler.agent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dev.CompilePerms;
import com.google.gwt.dev.CompilePerms.CompilePermsOptions;
import com.google.gwt.dev.util.log.PrintWriterTreeLogger;
import com.google.gwt.dist.compiler.agent.impl.CompilePermsOptionsImpl;
import com.google.gwt.dist.util.Util;
import com.google.gwt.dist.util.ZipDecompressor;

class Connect extends Thread {
	private File temporaryStorage = null; // Store source and precompile output.
	private Socket client = null;
	private InputStream is = null;
	private OutputStream os = null;
	private ZipDecompressor decompressor = null;

	private static Logger logger = Logger.getLogger(Connect.class.getName());

	public Connect() {
		initialize();
	}

	public Connect(Socket clientSocket) {
		initialize();
		client = clientSocket;
		try {
			is = client.getInputStream();
			os = client.getOutputStream();
		} catch (IOException e1) {
			try {
				client.close();
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
			return;
		}
		this.run();
	}

	public void run() {
		try {
			ByteArrayOutputStream receivedData = new ByteArrayOutputStream();
			byte[] buff = new byte[2056];
			int bytesRead = 0;
			while ((bytesRead = is.read(buff)) > -1) {
				receivedData.write(buff, 0, bytesRead);
			}
			decompressor.decompressAndStoreToFile(receivedData,
					temporaryStorage);
			receivedData.close();

			// close streams and connections
			is.close();
			os.close();
			client.close();
		} catch (Exception e) {
		}

		try {
			//File uncompressedSrc = new File("uncompressed" + File.separator + "war" + File.separator + "WEB-INF" + File.separator + "classes" + File.separator);
			File uncompressedSrc = new File("uncompressed" + File.separator + "src" + File.separator);
			//Util.addFolderToClasspath(uncompressedSrc, this.getContextClassLoader());
			try {
				Util.addUrl(uncompressedSrc.toURL());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TreeLogger logger = new PrintWriterTreeLogger();
			((PrintWriterTreeLogger) logger).setMaxDetail(TreeLogger.WARN);

			// Compile Perms using the input data stored in tempStorage.
			List<String> moduleNames = new ArrayList<String>();
			moduleNames.add("com.hypersimple.HyperSimple");
			File workDir = new File("uncompressed\\work");

			final CompilePermsOptions options = new CompilePermsOptionsImpl();

			options.setModuleNames(moduleNames);
			options.setWorkDir(workDir);
			int perms[] = { 0 };
			options.setPermsToCompile(perms);

			new CompilePerms(options).run(logger);
		} catch (UnableToCompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Method creates necessary folder and compression and/or decompression
	 * engine.
	 */
	private void initialize() {
		temporaryStorage = new File("uncompressed");
		temporaryStorage.mkdir();
		decompressor = new ZipDecompressor();
	}
}