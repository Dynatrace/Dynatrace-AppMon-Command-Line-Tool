package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameters;
import com.dynatrace.diagnostics.cmd.model.CmdOptions;
import com.dynatrace.sdk.server.exceptions.ServerConnectionException;
import com.dynatrace.sdk.server.exceptions.ServerResponseException;
import com.dynatrace.sdk.server.servermanagement.ServerManagement;

import static com.dynatrace.diagnostics.cmd.Constants.CMD_SHUTDOWN;
import static java.lang.System.out;

/**
 * @author Dariusz.Glugla
 */
@Parameters(commandNames = CMD_SHUTDOWN, commandDescription = "shuts down the Dynatrace Server")
public class Shutdown extends AbstractHostPortCommand {

	public static final String HEADER = "Shutdown";

	@Override public void runInternal(CmdOptions options) throws ServerConnectionException, ServerResponseException {
		if (new ServerManagement(getClient()).shutdown()) {
			out.println(" Server stopped sucessfully.");
		} else {
			out.println(" Unable to stop Server.");
		}
	}

	@Override protected String getHeader() {
		return HEADER;
	}
}
