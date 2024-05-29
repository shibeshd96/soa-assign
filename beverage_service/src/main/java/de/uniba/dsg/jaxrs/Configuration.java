package de.uniba.dsg.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class Configuration{
	private static final Logger LOGGER = Logger.getLogger(Configuration.class.getName());

	public static Properties loadProperties() {
		try (InputStream stream = ExamplesApi.class.getClassLoader().getResourceAsStream("config.properties")) {
			Properties properties = new Properties();
			properties.load(stream);
			return properties;
		} catch (IOException e) {
			LOGGER.severe("Cannot load configuration file.");
			return null;
		}
	}

	public static String getServerUri() {
		String serverUri = System.getenv("SERVER_URI");

		if (serverUri == null || serverUri.isEmpty()) {
			serverUri = loadProperties().getProperty("serverUri");
		}

		return serverUri;
	}

	public static String getDBHandlerUri() {
		String serverUri = System.getenv("DB_HANDLER_URI");

		if (serverUri == null || serverUri.isEmpty()) {
			serverUri = loadProperties().getProperty("dbhandlerUri");
		}

		return serverUri;
	}
}
