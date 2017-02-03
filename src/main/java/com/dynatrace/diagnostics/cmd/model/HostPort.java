package com.dynatrace.diagnostics.cmd.model;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameters;

/**
 * @author Dariusz.Glugla
 */
@Parameters(commandDescription = "[<hostname>[:<port>]]")
public class HostPort {

	private static final String SEPARATOR = ":";

	private final String host;
	private final Integer port;

	public HostPort(String host, Integer port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public Integer getPort() {
		return port;
	}

	class Converter implements IStringConverter<HostPort> {

		@Override public HostPort convert(String value) {
			if (value.contains(SEPARATOR)) {
				String[] s = value.split(":");
				return new HostPort(s[0], Integer.parseInt(s[1]));
			}
			return new HostPort(value, null);
		}
	}
}
