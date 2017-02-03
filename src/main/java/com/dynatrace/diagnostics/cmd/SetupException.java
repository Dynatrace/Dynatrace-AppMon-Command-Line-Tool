package com.dynatrace.diagnostics.cmd;


/**
 * Represents an exception that is thrown while setting up the environment, e.g. extracting keystore file.
 *
 * @author martin.wurzinger
 */
public class SetupException extends Exception {

	private static final long serialVersionUID = 1L;

	public SetupException(String message) {
		super(message);
	}

	public SetupException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override public String getMessage() {
		Throwable cause = getCause();
		if (cause == null) {
			return super.getMessage();
		}
		return cause.getMessage() + " " + super.getMessage();
	}
}
