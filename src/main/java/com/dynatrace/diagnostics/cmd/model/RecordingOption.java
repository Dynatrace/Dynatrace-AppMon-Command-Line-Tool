package com.dynatrace.diagnostics.cmd.model;

import com.beust.jcommander.IStringConverter;
import org.apache.commons.lang3.StringUtils;

import java.security.InvalidParameterException;

/**
 * @author Dariusz.Glugla
 */
public enum RecordingOption {

	ALL, VIOLATIONS, TIMESERIES;

	@Override
	public String toString() {
		return this.name().toLowerCase();
	}

	public class Converter implements IStringConverter<RecordingOption> {

		@Override public RecordingOption convert(String s) {
			if (StringUtils.isBlank(s)) {
				return null;
			}
			for (RecordingOption value : RecordingOption.values()) {
				if (value.toString().equals(s.toLowerCase())) {
					return value;
				}
			}
			return null;
		}
	}

	public com.dynatrace.sdk.server.sessions.models.RecordingOption toSDK() {
		switch (this) {
			case ALL:
				return com.dynatrace.sdk.server.sessions.models.RecordingOption.ALL;
			case VIOLATIONS:
				return com.dynatrace.sdk.server.sessions.models.RecordingOption.VIOLATIONS;
			case TIMESERIES:
				return com.dynatrace.sdk.server.sessions.models.RecordingOption.TIME_SERIES;
		}
		throw new InvalidParameterException("Uknown recording option: " + this.toString() + ".");
	}
}
