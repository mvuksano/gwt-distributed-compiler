package com.google.gwt.dist.compiler.agent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.google.gwt.dist.util.ZipDecompressor;

class Connect extends Thread {
	private Socket client = null;
	private InputStream is = null;
	private OutputStream os = null;

	public Connect() {
	}

	public Connect(Socket clientSocket) {
		client = clientSocket;
		try {
			is = client.getInputStream();
			os = client.getOutputStream();
		} catch (Exception e1) {
			try {
				client.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return;
		}
		this.run();
	}

	public void run() {
		try {
			ByteArrayOutputStream receivedData = new ByteArrayOutputStream();
			File dest = new File("helper.zip");
			dest.createNewFile();
			FileOutputStream fos = new FileOutputStream(dest);
			byte[] buff = new byte[2056];
			int bytesRead = 0;
			while ((bytesRead = is.read(buff)) > -1) {
				fos.write(buff, 0, bytesRead);
				receivedData.write(buff, 0, bytesRead);
			}
			fos.close();
			(new ZipDecompressor()).decompressAndStoreToFile(receivedData, new File("uncompressed"));
			receivedData.close();
			// close streams and connections
			is.close();
			os.close();
			client.close();
		} catch (Exception e) {
		}
	}
}