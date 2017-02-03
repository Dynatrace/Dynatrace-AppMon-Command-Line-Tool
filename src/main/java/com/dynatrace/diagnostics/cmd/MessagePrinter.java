package com.dynatrace.diagnostics.cmd;


import org.apache.commons.lang3.StringUtils;

/**
 * Prints messages of Command Line Tool to standard out and stores success and error messages
 *
 * @author Dariusz.Glugla
 * @author anita.engleder
 */
public final class MessagePrinter {

	private static String errorMessage = null;
	private static String successMessage = null;

	private MessagePrinter() {
		// prevent from creation
	}

	/**
	 * @return the error message of the recently executed <code>Cmd</code> command or <code>null</code> if no error message is
	 * available.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	private static void setErrorMessage(String message) {
		if (StringUtils.isEmpty(errorMessage)) {
			errorMessage = message.trim();
		} else {
			errorMessage += "\n" + message.trim();
		}
		successMessage = null;
	}

	/**
	 * @return the success message of the recently executed <code>Cmd</code> command or <code>null</code> if no success message is
	 * available.
	 */
	public String getSuccessMessage() {
		return successMessage;
	}

	private static void setSuccessMessage(String message) {
		if (StringUtils.isEmpty(successMessage)) {
			successMessage = message.trim();
		} else {
			successMessage += "\n" + message.trim();
		}
		errorMessage = null;
	}

	public static void resetMessages() {
		successMessage = null;
		errorMessage = null;
	}

	/**
	 * Prints given message, sets this.errorMessage and clears this.successMessage.
	 */
	public static void printlnErrorMessage(String message) {
		if (StringUtils.isBlank(message)) {
			return;
		}
		setErrorMessage(message);

		println(message);
	}

	/**
	 * Prints given message, sets this.successMessage and clears this.errorMessage.
	 */
	public static void printlnSuccessMessage(String message) {
		if (StringUtils.isBlank(message)) {
			return;
		}
		setSuccessMessage(message);

		println(message);
	}

	/**
	 * Prints given message and do not change this.successMessage and this.errorMessage.
	 */
	public static void println(String message) {
		System.out.println(message);
	}

	/**
	 * Prints given message without new line and do not change this.successMessage and this.errorMessage.
	 */
	public static void print(String message) {
		System.out.print(message);
	}
}
