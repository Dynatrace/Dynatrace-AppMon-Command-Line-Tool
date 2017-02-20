package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameters;
import com.dynatrace.diagnostics.cmd.model.CmdOptions;
import com.dynatrace.sdk.server.exceptions.ServerConnectionException;
import com.dynatrace.sdk.server.exceptions.ServerResponseException;
import com.dynatrace.sdk.server.servermanagement.ServerManagement;

import static com.dynatrace.diagnostics.cmd.Constants.CMD_RESTART;
import static java.lang.System.out;

/**
 * @author Dariusz.Glugla
 */
@Parameters(commandNames = CMD_RESTART, commandDescription = "restarts the Dynatrace Server")
public class Restart extends AbstractHostPortCommand {

	public static final String HEADER = "Restart";

	@Override public void runInternal(CmdOptions options) throws ServerConnectionException, ServerResponseException {
		if (new ServerManagement(getClient()).restart()) {
			out.println(" Server restarted sucessfully.");
		} else {
			out.println(" Unable to restart Server.");
		}
	}

	@Override protected String getHeader() {
		return HEADER;
	}
}
