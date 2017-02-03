package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameter;
import com.dynatrace.diagnostics.cmd.ServerConfig;
import com.dynatrace.diagnostics.cmd.model.HostPort;
import com.dynatrace.sdk.server.DynatraceClient;

import static com.dynatrace.diagnostics.cmd.Constants.*;

/**
 * @author Dariusz.Glugla
 */
public abstract class AbstractHostPortCommand extends AbstractCommand {

	@Parameter(names = OPT_USER, description = "specifies the username to log in with")
	private String user;
	@Parameter(names = OPT_PASS, description = "specifies the password to log in with", password = true)
	private String password;
	@Parameter(names = FLAG_NO_SSL, description = "disables communication via HTTPS")
	private Boolean noSSL;
	@Parameter(description = "[<hostname>[:<port>]]")
	private HostPort hostPort;

	private DynatraceClient dtClient = null;

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public Boolean getNoSSL() {
		return noSSL;
	}

	public HostPort getHostPort() {
		return hostPort;
	}

	protected DynatraceClient getClient() {
		if (dtClient == null) {
			dtClient = new DynatraceClient(new ServerConfig(this));
		}
		return dtClient;
	}
}
