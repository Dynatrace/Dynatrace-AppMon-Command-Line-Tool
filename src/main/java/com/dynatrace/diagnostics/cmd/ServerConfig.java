package com.dynatrace.diagnostics.cmd;

import com.dynatrace.diagnostics.cmd.commands.AbstractHostPortCommand;
import com.dynatrace.sdk.server.ServerConfiguration;
import org.apache.commons.lang3.StringUtils;

import java.io.Console;
import java.security.Principal;

import static com.dynatrace.diagnostics.cmd.Constants.CONNECTION_TIMEOUT;

/**
 * @author Dariusz.Glugla
 */
public class ServerConfig implements ServerConfiguration {

	private final String host;
	private final int port;
	private final boolean sslEnabled;
	private String userName;
	private String password;

	public ServerConfig(AbstractHostPortCommand cmdLineSettings) {
		this(Settings.load(), cmdLineSettings);
	}

	private ServerConfig(Settings userSettings, AbstractHostPortCommand cmdLineSettings) {
		this.host =
				StringUtils.isBlank(cmdLineSettings.getHostPort().getHost()) ? userSettings.getServerHost() :
						cmdLineSettings.getHostPort().getHost();
		this.port =
				cmdLineSettings.getHostPort().getPort().intValue() <= 0 ?
						(userSettings.isSslEnabled() ? userSettings.getServerPortHttps() : userSettings.getServerPortHttp()) :
						cmdLineSettings.getHostPort().getPort();
		this.sslEnabled =
				cmdLineSettings.getNoSSL() == null ? userSettings.isSslEnabled() : !cmdLineSettings.getNoSSL().booleanValue();
		this.userName = StringUtils.isBlank(cmdLineSettings.getUser()) ? userSettings.getUserName() : cmdLineSettings.getUser();
		this.password =
				StringUtils.isBlank(cmdLineSettings.getPassword()) ? userSettings.getPassword() : cmdLineSettings.getPassword();
	}

	@Override public String getHost() {
		return host;
	}

	@Override public boolean isSSL() {
		return sslEnabled;
	}

	@Override public int getPort() {
		return port;
	}

	@Override public int getTimeout() {
		return CONNECTION_TIMEOUT;
	}

	@Override public boolean isValidateCertificates() {
		return false;
	}

	@Override public Principal getUserPrincipal() {
		if (StringUtils.isBlank(userName)) {
			userName = readUserName();
		}
		return new Principal() {

			@Override public String getName() {
				return userName;
			}
		};
	}

	@Override public String getPassword() {
		if (StringUtils.isBlank(password)) {
			password = readPassword();
		}
		return password;
	}

	@Override public String getName() {
		return null;
	}

	/**
	 * Prompt user to enter user name and read value.
	 *
	 * @author martin.wurzinger
	 */
	private static String readUserName() {
		Console console = System.console();
		if (console == null) { //is null if cmd is executed in eclipse -> use the option "-pass" here
			System.err.println(" Error reading user name.");
			return StringUtils.EMPTY;
		}
		return new String(console.readLine(" User: "));
	}

	/**
	 * Prompt user to enter password and read value.
	 *
	 * @return the entered password
	 * @author anita.engleder
	 */
	private static String readPassword() {
		Console console = System.console();
		if (console == null) { //is null if cmd is executed in eclipse -> use the option "-pass" here
			System.err.println(" Error reading password.");
			return StringUtils.EMPTY;
		}
		return new String(console.readPassword(" Password: "));
	}

}
