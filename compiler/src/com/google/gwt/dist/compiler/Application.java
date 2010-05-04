package com.google.gwt.dist.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.stream.StreamSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;

public class Application {

	private Marshaller marshaller;
	private Unmarshaller unmarshaller;
	private AgentsSettings settings;
	private static String AGENTS_SETTINGS_FILE_LOCATION = "config/config.xml";  
	
	private static final Logger logger = Logger.getLogger(Application.class.getName());

	public static void main(String[] args) {
		ApplicationContext appContext = new FileSystemXmlApplicationContext(
				new File("config/applicationContext.xml").toString());
		Application application = (Application) appContext
				.getBean("application");
		// application.saveSettings();
		application.loadSettings();
	}
	
	public Marshaller getMarshaller() {
		return this.marshaller;
	}
	
	public AgentsSettings getSettings() {
		return this.settings;
	}
	
	public Unmarshaller getUnmarshaller() {
		return this.unmarshaller;
	}

	/**
	 * Loads settings from Application.AGENTS_SETTINGS_FILE_LOCATION
	 */
	protected void loadSettings() {
		FileInputStream is = null;
		try {
			is = new FileInputStream(new File(AGENTS_SETTINGS_FILE_LOCATION));
			this.settings = (AgentsSettings) this.unmarshaller
					.unmarshal(new StreamSource(is));
		} catch (XmlMappingException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, e.getMessage());
				}
			}
		}
	}
	
	public void setMarshaller(Marshaller m) {
		this.marshaller = m;
	}
	
	public void setUnmarshaller(Unmarshaller um) {
		this.unmarshaller = um;
	}
}
