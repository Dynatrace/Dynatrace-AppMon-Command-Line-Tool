package com.dynatrace.diagnostics.cmd;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.dynatrace.diagnostics.cmd.MessagePrinter.println;


public class Settings {

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

	private static boolean logWarnings = false;

	private Settings(String serverHost, int serverPortHttp, int serverPortHttps, boolean sslEnabled, String userName,
			String password) {
		this.serverHost = serverHost;
		this.serverPortHttp = serverPortHttp;
		this.serverPortHttps = serverPortHttps;
		this.sslEnabled = sslEnabled;
		this.userName = userName;
		this.password = password;
	}

	public static Settings load(boolean logWarnings) {
		Settings.logWarnings = logWarnings;
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
			if (logWarnings) {
				e.printStackTrace();
				println(" Unable to parse properties: " + e.getMessage());
			}
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
				if (logWarnings) {
					println(" Unable to find the default property file.");
				}
			} else {
				destination.load(defaultPropertiesInStream);
			}
		} catch (IOException ioe) {
			if (logWarnings) {
				println(" Unable to read the default property file.");
			}
		} finally {
			if (defaultPropertiesInStream != null) {
				try {
					defaultPropertiesInStream.close();
				} catch (IOException e) {
					if (logWarnings) {
						println(" Unable to close the default property file.");
					}
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

				println(" Loading customized properties from " + customerPropertiesFile.getAbsolutePath());
				println("");

				destination.load(customerPropertiesInStream);
			}
		} catch (Exception e) { //we do not want the cmd tool to let any errors slip outside
			if (logWarnings) {
				e.printStackTrace();
				println(" Unable to read customized properties: " + e.getMessage());
			}
		} finally {
			if (customerPropertiesInStream != null) {
				try {
					customerPropertiesInStream.close();
				} catch (IOException e) {
					if (logWarnings) {
						e.printStackTrace();
						println(" Unable to close the customized property file: " + e.getMessage());
					}
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
				if (logWarnings) {
					println(" Invalid value '" + sValue + "' for property '" + property + "', using default value '"
							+ defaultValue + "'.");
				}
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
				if (logWarnings) {
					println(" Invalid value '" + sValue + "' for property '" + property + "', using default value '"
							+ defaultValue + "'.");
				}
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
		if (logWarnings) {
			println(" Property '" + property + "' not found, using default value '" + defaultValue + "'.");
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
