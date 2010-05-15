package com.google.gwt.dist.compiler.impl;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import com.google.gwt.dist.Node;
import com.google.gwt.dist.ProcessingState;
import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.comm.CommMessageResponse;
import com.google.gwt.dist.comm.ProcessingStateResponse;
import com.google.gwt.dist.comm.CommMessage.CommMessageType;
import com.google.gwt.dist.compiler.SessionManager;
import com.google.gwt.dist.compiler.communicator.Communicator;
import com.google.gwt.dist.impl.ProcessingStateMessage;
import com.google.gwt.dist.util.ZipCompressor;
import com.google.gwt.dist.util.ZipDecompressor;

/**
 * Concrete SessionManager implementation.
 */
public class SessionManagerImpl implements SessionManager {

	private ZipCompressor compressor;
	private ZipDecompressor decompressor;
	private Communicator communicator;
	private Node node;

	public SessionManagerImpl(Communicator communicator, Node node,
			ZipCompressor compressor, ZipDecompressor decompressor) {
		this.compressor = compressor;
		this.decompressor = decompressor;
		this.communicator = communicator;
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

	public <T extends CommMessageResponse> T sendMessageToAgent(
			CommMessage<T> message) {
		T response = communicator.sendMessage(message, this.node);
		return response;

	}

	@Override
	public Communicator getCommunicator() {
		return this.communicator;
	}

	@Override
	public void setCommunicator(Communicator communicator) {
		this.communicator = communicator;
	}

	public void start() {
		ProcessingStateResponse response = sendMessageToAgent(new ProcessingStateMessage(
				CommMessageType.QUERY));
		ProcessingState currentState = null;
		if (response != null) {
			currentState = ((ProcessingStateResponse) response)
					.getCurrentState();
			if (currentState != null) {
				switch (currentState) {
				case READY:
					communicator.sendData(generateDataForProcessing(),
							this.node);
					break;
				case INPROGRESS:
					System.out.println("Agent" + this.node.getIpaddress()
							+ " is in progress.");
					break;
				case COMPLETED:
					try {
					byte[] retrievedData = communicator.retrieveData(this.node);
					File temp = new File("uncompressed");
					decompressor.decompressAndStoreToFile(retrievedData, temp);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
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

		compressor.setExcludePattern(Pattern
				.compile("bin|\\.settings\\.classpath\\.project"));

		return compressor.archiveAndCompressDir(source).toByteArray();
	}
}
