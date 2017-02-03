package com.dynatrace.diagnostics.cmd.model;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.IStringConverterFactory;

/**
 * @author Dariusz.Glugla
 */
public class Factory implements IStringConverterFactory {

	@Override public Class<? extends IStringConverter<?>> getConverter(Class forType) {
		if (forType.equals(HostPort.class)) {
			return HostPort.Converter.class;
		}
		if (forType.equals(RecordingOption.class)) {
			return RecordingOption.Converter.class;
		}
		return null;
	}
}
