package com.dynatrace.diagnostics.cmd.model;

import com.beust.jcommander.IStringConverter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Dariusz.Glugla
 */
public enum DumpMemType {

	SIMPLE, EXTENDED, SELECTIVE;

	@Override
	public String toString() {
		return this.name().toLowerCase();
	}

	public class Converter implements IStringConverter<DumpMemType> {

		@Override public DumpMemType convert(String s) {
			if (StringUtils.isBlank(s)) {
				return null;
			}
			for (DumpMemType value : DumpMemType.values()) {
				if (value.toString().equals(s.toLowerCase())) {
					return value;
				}
			}
			return null;
		}
	}
//
//	public StoredSessionType toSDK() {
//		switch (this) {
//			case SIMPLE:
//				return StoredSessionType.SIMPLE;
//			case EXTENDED:
//				return StoredSessionType.EXTENDED;
//			case SELECTIVE:
//				return StoredSessionType.SELECTIVE;
//		}
//		throw new InvalidParameterException("Uknown session type: " + this.toString() + ".");
//	}
}
