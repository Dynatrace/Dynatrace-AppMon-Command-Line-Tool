package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameters;
import com.dynatrace.diagnostics.cmd.model.CmdOptions;
import com.dynatrace.sdk.server.exceptions.ServerConnectionException;
import com.dynatrace.sdk.server.exceptions.ServerResponseException;
import com.dynatrace.sdk.server.servermanagement.ServerManagement;

import static com.dynatrace.diagnostics.cmd.Constants.CMD_SHUTDOWN;
import static com.dynatrace.diagnostics.cmd.MessagePrinter.printlnErrorMessage;
import static com.dynatrace.diagnostics.cmd.MessagePrinter.printlnSuccessMessage;

/**
 * @author Dariusz.Glugla
 */
@Parameters(commandNames = CMD_SHUTDOWN, commandDescription = "shuts down the Dynatrace Server")
public class Shutdown extends AbstractHostPortCommand {

	@Override public void run(CmdOptions options) throws ServerConnectionException, ServerResponseException {
		final StringBuilder sb = new StringBuilder();
		sb.append(" --- Shutdown --- \n\n");
		sb.append(" stopping server.... \n\n");

		if (new ServerManagement(getClient()).shutdown()) {
			sb.append(" Server stopped sucessfully.\n");
			printlnSuccessMessage(sb.toString());
		} else {
			sb.append(" Unable to stop Server.\n");
			printlnErrorMessage(sb.toString());
		}
	}
}
