package com.google.gwt.dist.compiler.agent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
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

			File uncompressedSrc = new File("uncompressed" + File.separator
					+ "src" + File.separator);
			Util.addUrl(uncompressedSrc.toURI().toURL());
			TreeLogger logger = new PrintWriterTreeLogger();
			((PrintWriterTreeLogger) logger).setMaxDetail(TreeLogger.INFO);

			// Compile Perms using the input data stored in tempStorage.
			List<String> moduleNames = new ArrayList<String>();
			moduleNames.add("com.hypersimple.HyperSimple");
			File workDir = new File("uncompressed" + File.separator + "work");

			final CompilePermsOptions options = new CompilePermsOptionsImpl();

			options.setModuleNames(moduleNames);
			options.setWorkDir(workDir);
			int perms[] = { 0, 1, 2, 3, 4, 5 };
			options.setPermsToCompile(perms);

			new CompilePerms(options).run(logger);
		} catch (MalformedURLException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} catch (UnableToCompleteException e) {
			logger.log(Level.SEVERE, e.getMessage());
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