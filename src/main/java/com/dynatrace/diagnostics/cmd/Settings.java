package com.dynatrace.diagnostics.cmd;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Settings {

	private static final Logger logger = Logger.getLogger(Settings.class.getName());

	private static final String PROPERTIES_FILE = "cmd.properties";
	private static final String PROPERTY_SERVER_HOST = "server.host";
	private static final String PROPERTY_SERVER_PORT_HTTP = "server.port.http";
	private static final String PROPERTY_SERVER_PORT_HTTPS = "server.port.https";
	private static final String PROPERTY_SSL = "server.ssl";
	private static final String PROPERTY_USER = "user";
	private static final String PROPERTY_PASS = "pass";
	private static final String PROPERTY_TRUSTSTORE = "javax.net.ssl.trustStore";
	private static final String PROPERTY_TRUSTSTORE_PASSWORD = "javax.net.ssl.trustStorePassword";
	private static final String DEFAULT_KEYSTORE_LOCATION = "keystore.jks";
	private static final String DEFAULT_SSL_KEYSTORE_PASS = "c2wftIxPUsFGHsLMV4HA";
	private static final String DEFAULT_SERVER_HOST = "localhost";
	private static final int DEFAULT_SERVER_PORT = 8020;
	private static final int DEFAULT_SERVER_SSL_PORT = 8021;
	private static final boolean DEFAULT_SSL = true;

	private final String serverHost;
	private final int serverPortHttp;
	private final int serverPortHttps;
	private final boolean sslEnabled;
	private final String userName;
	private final String password;

	private Settings(String serverHost, int serverPortHttp, int serverPortHttps, boolean sslEnabled, String userName,
			String password) {
		this.serverHost = serverHost;
		this.serverPortHttp = serverPortHttp;
		this.serverPortHttps = serverPortHttps;
		this.sslEnabled = sslEnabled;
		this.userName = userName;
		this.password = password;
	}

	public static Settings load() {
		Properties properties = new Properties();

		readDefaultPropertiesFile(properties);
		readCustomerPropertiesFile(properties);

		return load(properties);
	}

	private static Settings load(Properties properties) {
		try {
			boolean sslEnabled = readBooleanProperty(properties, PROPERTY_SSL, DEFAULT_SSL);
			int serverPortHttps = readIntProperty(properties, PROPERTY_SERVER_PORT_HTTPS, DEFAULT_SERVER_SSL_PORT);
			int serverPortHttp = readIntProperty(properties, PROPERTY_SERVER_PORT_HTTP, DEFAULT_SERVER_PORT);
			String serverHost = readStringProperty(properties, PROPERTY_SERVER_HOST, DEFAULT_SERVER_HOST);
			String userName = readStringProperty(properties, PROPERTY_USER, StringUtils.EMPTY);
			String password = readStringProperty(properties, PROPERTY_PASS, StringUtils.EMPTY);

			String trustStoreDefaultValue = System.getProperty(PROPERTY_TRUSTSTORE);
			if (trustStoreDefaultValue == null) {
				trustStoreDefaultValue = DEFAULT_KEYSTORE_LOCATION;
			}
			System.setProperty(PROPERTY_TRUSTSTORE, readStringProperty(properties, PROPERTY_TRUSTSTORE, trustStoreDefaultValue));

			String trustStorePwdDefaultValue = System.getProperty(PROPERTY_TRUSTSTORE_PASSWORD);
			if (trustStorePwdDefaultValue == null) {
				trustStorePwdDefaultValue = DEFAULT_SSL_KEYSTORE_PASS;
			}
			System.setProperty(PROPERTY_TRUSTSTORE_PASSWORD,
					readStringProperty(properties, PROPERTY_TRUSTSTORE_PASSWORD, trustStorePwdDefaultValue));

			return new Settings(serverHost, serverPortHttp, serverPortHttps, sslEnabled, userName, password);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Unable to parse properties.", e);
		}
		return new Settings(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT, DEFAULT_SERVER_SSL_PORT, DEFAULT_SSL, StringUtils.EMPTY,
				StringUtils.EMPTY);
	}

	private static void readDefaultPropertiesFile(Properties destination) {
		InputStream defaultPropertiesInStream = null;

		try {
			// load default values from property file if additionally bundled
			defaultPropertiesInStream = Settings.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);

			if (defaultPropertiesInStream == null) {
				logger.log(Level.WARNING, "Unable to find the default property file.");
			} else {
				destination.load(defaultPropertiesInStream);
			}
		} catch (IOException ioe) {
			logger.log(Level.WARNING, "Unable to read the default property file.");
		} finally {
			if (defaultPropertiesInStream != null) {
				try {
					defaultPropertiesInStream.close();
				} catch (IOException e) {
					logger.log(Level.WARNING, "Unable to close the default property file.");
				}
			}
		}
	}

	private static void readCustomerPropertiesFile(Properties destination) {
		InputStream customerPropertiesInStream = null;

		try {
			// load user defined properties if existing
			File customerPropertiesFile = new File(PROPERTIES_FILE);
			if (customerPropertiesFile.exists()) {
				customerPropertiesInStream = new FileInputStream(new File(PROPERTIES_FILE));

				System.out.println(" Loading customized properties from " + customerPropertiesFile.getAbsolutePath());
				System.out.println();

				destination.load(customerPropertiesInStream);
			}
		} catch (Exception e) { //we do not want the cmd tool to let any errors slip outside
			logger.log(Level.WARNING, "Unable to read customized properties.");
		} finally {
			if (customerPropertiesInStream != null) {
				try {
					customerPropertiesInStream.close();
				} catch (IOException e) {
					logger.log(Level.WARNING, "Unable to close the customized property file.");
				}
			}
		}
	}

	private static int readIntProperty(Properties properties, String property, int defaultValue) {
		String sValue = (String) properties.get(property);
		if (StringUtils.isNotBlank(sValue)) {
			try {
				return Integer.parseInt(sValue);
			} catch (NumberFormatException e) {
				logger.log(Level.WARNING,
						"Invalid value '" + sValue + "' for property '" + property + "', using default value '" + defaultValue
								+ "'.");
				return defaultValue;
			}
		}
		logMissingProperty(property, defaultValue);
		return defaultValue;
	}

	private static boolean readBooleanProperty(Properties properties, String property, boolean defaultValue) {
		String sValue = (String) properties.get(property);
		if (StringUtils.isNotBlank(sValue)) {
			try {
				return Boolean.valueOf(sValue).booleanValue();
			} catch (NumberFormatException e) {
				logger.log(Level.WARNING,
						"Invalid value '" + sValue + "' for property '" + property + "', using default value '" + defaultValue
								+ "'.");
				return defaultValue;
			}
		}
		logMissingProperty(property, defaultValue);
		return defaultValue;
	}

	private static String readStringProperty(Properties properties, String property, String defaultValue) {
		String sValue = (String) properties.get(property);
		if (StringUtils.isNotBlank(sValue)) {
			return sValue;
		}
		logMissingProperty(property, defaultValue);
		return defaultValue;
	}

	private static void logMissingProperty(String property, Object defaultValue) {
		if (logger.isLoggable(Level.FINE)) {
			logger.log(Level.FINE, "Property '" + property + "' not found, using default value '" + defaultValue + "'.");
		}
	}

	void extractKeyStoreIfNeeded() throws SetupException {
		// extract keystore file only if SSL is actually used
		if (!sslEnabled) {
			return;
		}

		// read system property javax.net.ssl.trustStore
		String trustStoreProperty = null;
		try {
			trustStoreProperty = System.getProperty(PROPERTY_TRUSTSTORE);
			if (trustStoreProperty == null) {
				throw new IllegalStateException("No system property '" + PROPERTY_TRUSTSTORE + "' defined");
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "Unable to read system property " + PROPERTY_TRUSTSTORE);
			return;
		}

		// do not extract keystore file if user has specified a customized keystore
		if (!DEFAULT_KEYSTORE_LOCATION.equals(trustStoreProperty)) {
			return;
		}

		File keystoreFile = new File(DEFAULT_KEYSTORE_LOCATION);

		// if default keystore file already exists, extraction will not be necessary
		if (keystoreFile.exists()) {
			return;
		}

		URL url = null;
		try {
			url = this.getClass().getClassLoader().getResource(DEFAULT_KEYSTORE_LOCATION);
		} catch (Exception e) {
			throw new SetupException("Unable to find keystore file for extraction.", e);
		}
		if (url == null) {
			throw new SetupException("Unable to find keystore file for extraction.");
		}

		byte[] buffer = new byte[1024];
		FileOutputStream fos = null;
		InputStream openStream = null;

		try {
			if (!keystoreFile.createNewFile()) {
				throw new SetupException("Unable to extract keystore file.");
			}
		} catch (IOException e) {
			throw new SetupException(
					"Unable to extract keystore file. Please run command as privileged user if you are not allowed to write to '"
							+ keystoreFile.getAbsolutePath() + "'.", e);
		}

		try {
			fos = new FileOutputStream(keystoreFile);
			openStream = url.openStream();

			int read = openStream.read(buffer);
			while (read != -1) {
				fos.write(buffer, 0, read);
				read = openStream.read(buffer);
			}
			fos.close();
			fos = null;

			System.out.println(" Keystore successfully extracted to " + keystoreFile.getAbsolutePath());
			System.out.println();

			openStream.close();
			openStream = null;

		} catch (Exception e) {
			throw new SetupException("Unable to extract keystore file. SSL connections will not work.", e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException ioe) {
				}
			}
			if (openStream != null) {
				try {
					openStream.close();
				} catch (IOException ioe) {
				}
			}
		}
	}

	public String getServerHost() {
		return serverHost;
	}

	public int getServerPortHttp() {
		return serverPortHttp;
	}

	public int getServerPortHttps() {
		return serverPortHttps;
	}

	public boolean isSslEnabled() {
		return sslEnabled;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}
}
