package com.dynatrace.diagnostics.cmd.model;

import com.beust.jcommander.IStringConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author Dariusz.Glugla
 */
public class AgentPattern {

	private static final char HOST_NAME_SEPARATOR = '@';
	private static final char PROCESS_ID_SEPARATOR = ':';

	public final String agentName;
	public final String hostName;
	public final Integer processId;

	public AgentPattern(String agentName, String hostName, Integer processId) {
		this.agentName = agentName;
		this.hostName = hostName;
		this.processId = processId;
	}

	public String getAgentName() {
		return agentName;
	}

	public String getHostName() {
		return hostName;
	}

	public Integer getProcessId() {
		return processId;
	}

	static class Converter implements IStringConverter<AgentPattern> {

		@Override public AgentPattern convert(String value) {
			if (StringUtils.isBlank(value)) {
				throw new InternalError("Error while parsing agent: invalid pattern '" + value + "'");
			}

			String agentName = value;
			String hostName = null;
			Integer processId = null;

			int hostNameSeparatorIndex = value.indexOf(HOST_NAME_SEPARATOR);
			int processIdSeparatorIndex = value.indexOf(PROCESS_ID_SEPARATOR);
			if (hostNameSeparatorIndex > 0) {
				agentName = value.substring(0, hostNameSeparatorIndex);
				if (processIdSeparatorIndex > hostNameSeparatorIndex) {
					hostName = value.substring(hostNameSeparatorIndex + 1, processIdSeparatorIndex);
					try {
						processId = Integer.parseInt(value.substring(processIdSeparatorIndex + 1));
					} catch (NumberFormatException e) {
						throw new InternalError("Error while parsing agent: invalid process id '" + e.getMessage() + "'");
					}
				} else if (processIdSeparatorIndex < 0) {
					hostName = value.substring(hostNameSeparatorIndex + 1);
				} else {
					throw new InternalError("Error while parsing agent: invalid pattern '" + value + "'");
				}
				if (StringUtils.isBlank(hostName)) {
					throw new InternalError("Error while parsing agent: empty host '" + value + "'");
				}
			} else if (hostNameSeparatorIndex == 0) {
				throw new InternalError("Error while parsing agent: empty agent name '" + value + "'");
			} else {
				throw new InternalError("Error while parsing agent: invalid pattern '" + value + "'");
			}
			return new AgentPattern(agentName, hostName, processId);
		}
	}

	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		AgentPattern that = (AgentPattern) o;

		return new EqualsBuilder()
				.append(agentName, that.agentName)
				.append(hostName, that.hostName)
				.append(processId, that.processId)
				.isEquals();
	}

	@Override public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(agentName)
				.append(hostName)
				.append(processId)
				.toHashCode();
	}

//	public com.dynatrace.sdk.server.memorydumps.models.AgentPattern toSDK() {
//		return new com.dynatrace.sdk.server.memorydumps.models.AgentPattern(agentName, hostName, processId);
//	}
}
