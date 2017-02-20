package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameter;
import com.dynatrace.diagnostics.cmd.Settings;
import com.dynatrace.diagnostics.cmd.model.HostPort;
import com.dynatrace.sdk.server.BasicServerConfiguration;
import com.dynatrace.sdk.server.DynatraceClient;
import com.dynatrace.sdk.server.ServerConfiguration;
import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.dynatrace.diagnostics.cmd.Constants.*;
import static com.dynatrace.sdk.server.BasicServerConfiguration.DEFAULT_CONNECTION_TIMEOUT;

/**
 * @author Dariusz.Glugla
 */
abstract class AbstractHostPortCommand extends AbstractCommand {

	@Parameter(names = OPT_USER, description = "username to log in with")
	private String user;
	@Parameter(names = OPT_PASS, description = "password to log in with")
	private String password;
	@Parameter(names = FLAG_NO_SSL, description = "disables communication via HTTPS")
	private Boolean noSSL;
	@Parameter(description = "[<hostname>[:<port>]]", converter = HostPort.Converter.class)
	private List<HostPort> hostPorts = new ArrayList<>(1);

	private ServerConfiguration serverConfig = null;
	private DynatraceClient dtClient = null;

	private HostPort getHostPort() {
		if (CollectionUtils.isEmpty(hostPorts)) {
			return null;
		}
		Preconditions.checkArgument(CollectionUtils.size(hostPorts) == 1, "Multiple hosts are not allowed");
		return hostPorts.get(0);
	}

	DynatraceClient getClient() {
		if (dtClient == null) {
			ServerConfiguration serverConfig = getServerConfig();
			dtClient = new DynatraceClient(serverConfig);
		}
		return dtClient;
	}

	private ServerConfiguration getServerConfig() {
		if (serverConfig == null) {
			Settings userSettings = Settings.load(BooleanUtils.isTrue(getVerbose()));

			final HostPort hostPort = getHostPort();
			final String host =
					hostPort == null || StringUtils.isBlank(hostPort.getHost()) ? userSettings.getServerHost() :
							hostPort.getHost();
			final int port = hostPort == null || hostPort.getPort() <= 0 ?
					(userSettings.isSslEnabled() ? userSettings.getServerPortHttps() : userSettings.getServerPortHttp()) :
					hostPort.getPort();
			final boolean sslEnabled = noSSL == null ? userSettings.isSslEnabled() : BooleanUtils.isFalse(noSSL);
			final String userName = StringUtils.isBlank(user) ? userSettings.getUserName() : user;
			final String password = StringUtils.isBlank(this.password) ? userSettings.getPassword() : this.password;

			serverConfig =
					new BasicServerConfiguration(userName, password, sslEnabled, host, port, false, DEFAULT_CONNECTION_TIMEOUT);
		}
		return serverConfig;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public Boolean getNoSSL() {
		return noSSL;
	}

	public List<HostPort> getHostPorts() {
		return hostPorts;
	}
}
