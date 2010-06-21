package com.google.gwt.dist.compiler.impl;

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

import org.apache.log4j.Logger;

import com.google.gwt.dist.Node;
import com.google.gwt.dist.ProcessingState;
import com.google.gwt.dist.SessionManager;
import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.comm.CommMessagePayload;
import com.google.gwt.dist.comm.ProcessingResultPayload;
import com.google.gwt.dist.comm.ProcessingStatePayload;
import com.google.gwt.dist.comm.SendDataPayload;
import com.google.gwt.dist.comm.CommMessage.CommMessageType;
import com.google.gwt.dist.comm.impl.ProcessingResultMessage;
import com.google.gwt.dist.comm.impl.ProcessingStateMessage;
import com.google.gwt.dist.comm.impl.SendDataMessage;
import com.google.gwt.dist.compiler.communicator.Communicator;
import com.google.gwt.dist.util.ZipCompressor;
import com.google.gwt.dist.util.ZipDecompressor;
import com.google.gwt.dist.util.options.DistCompilePermsOptions;

/**
 * Concrete SessionManager implementation.
 */
public class SessionManagerImpl implements SessionManager {

	private Communicator communicator;
	private ZipCompressor compressor;
	private ZipDecompressor decompressor;
	private DistCompilePermsOptions distCompilePermsOptions;
	private boolean finished;
	private Node node;
	
	private static final Logger logger = Logger.getLogger(SessionManagerImpl.class);

	public SessionManagerImpl(Communicator communicator, Node node,
			DistCompilePermsOptions options, ZipCompressor compressor,
			ZipDecompressor decompressor) {
		this.distCompilePermsOptions = options;
		this.compressor = compressor;
		this.decompressor = decompressor;
		this.communicator = communicator;
		this.finished = false;
		this.node = node;
	}

	@Override
	public boolean compilePermsCompleted(Node node) {
		return false;
	}

	@Override
	public boolean readyToReceiveData(Node node) {
		return false;
	}

	@Override
	public void run() {
	}

	@Override
	public void sendDataToAgent(byte[] data) {
		communicator.sendData(data, node);
	}

	public <T extends CommMessagePayload> T sendMessageToAgent(
			CommMessage<T> message) {
		T response = communicator.sendMessage(message, this.node);
		return response;

	}

	@Override
	public Communicator getCommunicator() {
		return this.communicator;
	}
	
	public boolean isFinished() {
		return this.finished;
	}

	@Override
	public void setCommunicator(Communicator communicator) {
		this.communicator = communicator;
	}

	public void start() {
		ProcessingStatePayload response = sendMessageToAgent(new ProcessingStateMessage(
				CommMessageType.QUERY));
		ProcessingState currentState = null;
		if (response != null) {
			currentState = ((ProcessingStatePayload) response)
					.getCurrentState();
			if (currentState != null) {
				switch (currentState) {
				case READY: {
					SendDataMessage message = new SendDataMessage();
					SendDataPayload payload = new SendDataPayload();
					payload.setPayload(generateDataForProcessing());
					payload.setCompilePermsOptions(distCompilePermsOptions);
					payload.setUUID(distCompilePermsOptions.getUUID());
					message.setResponse(payload);
					communicator.sendMessage(message, this.node);
					this.finished = false;
					break;
				}
				case INPROGRESS: {
					logger.info("Agent " + this.node.getIpaddress()
							+ " is in progress.");
					this.finished = false;
					break;
				}
				case COMPLETED: {
					try {
						ProcessingResultMessage message = new ProcessingResultMessage();
						ProcessingResultPayload payload = new ProcessingResultPayload();
						payload.setUUID(distCompilePermsOptions.getUUID());
						message.setResponse(payload);
						byte[] retrievedData = communicator.retrieveData(
								message, this.node);
						File temp = new File("work");
						decompressor.decompressAndStoreToFile(retrievedData,
								temp);
					} catch (IOException e) {
						e.printStackTrace();
					}
					logger.info("Agent " + this.node.getIpaddress()
							+ " has finished.");
					this.finished = true;
					break;
				}
				default:
					this.finished = false;
				}
			}
		}
	}

	/**
	 * Generates data to be processed by the agent.
	 * 
	 * @return
	 */
	private byte[] generateDataForProcessing() {
		File source = new File(System.getProperty("user.dir"));

		ByteArrayOutputStream srcFolder = compressor.archiveAndCompressDir(
				new File(source + File.separator + "src"), true);

		ByteArrayOutputStream workFolder = compressor.archiveAndCompressDir(
				new File(source + File.separator + "work"), true);

		ByteArrayOutputStream libFolder = compressor.archiveAndCompressDir(
				new File(source + File.separator
						+ distCompilePermsOptions.getGwtClassPath()), true,
				Pattern.compile("^(?:(?!gwt-servlet\\.jar).)*$"));

		ByteArrayInputStream bais1 = new ByteArrayInputStream(srcFolder
				.toByteArray());
		CheckedInputStream checksum1 = new CheckedInputStream(bais1,
				new Adler32());
		ZipInputStream zis1 = new ZipInputStream(checksum1);

		ByteArrayInputStream bais2 = new ByteArrayInputStream(workFolder
				.toByteArray());
		CheckedInputStream checksum2 = new CheckedInputStream(bais2,
				new Adler32());
		ZipInputStream zis2 = new ZipInputStream(checksum2);

		ByteArrayInputStream bais3 = new ByteArrayInputStream(libFolder
				.toByteArray());
		CheckedInputStream checksum3 = new CheckedInputStream(bais3,
				new Adler32());
		ZipInputStream zis3 = new ZipInputStream(checksum3);

		ZipInputStream mergedStream = compressor.mergeZippedStreams(zis1, zis2,
				zis3);

		ByteArrayOutputStream dataAsByteArrayOutputStream = new ByteArrayOutputStream();
		ZipOutputStream compressedResultStream = new ZipOutputStream(
				dataAsByteArrayOutputStream);

		try {
			ZipEntry ze = null;
			while ((ze = mergedStream.getNextEntry()) != null) {
				compressedResultStream.putNextEntry(ze);
				byte[] buff = new byte[2048];
				int bytesRead = 0;
				while ((bytesRead = mergedStream.read(buff)) > -1) {
					compressedResultStream.write(buff, 0, bytesRead);
				}
				compressedResultStream.flush();
				compressedResultStream.closeEntry();
				mergedStream.closeEntry();
			}
			compressedResultStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			zis1.close();
			zis2.close();
			bais1.close();
			bais2.close();
			workFolder.close();
			srcFolder.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dataAsByteArrayOutputStream.toByteArray();
	}
}
