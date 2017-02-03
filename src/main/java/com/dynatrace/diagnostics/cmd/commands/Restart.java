package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameters;
import com.dynatrace.diagnostics.cmd.model.CmdOptions;
import com.dynatrace.sdk.server.exceptions.ServerConnectionException;
import com.dynatrace.sdk.server.exceptions.ServerResponseException;
import com.dynatrace.sdk.server.servermanagement.ServerManagement;

import static com.dynatrace.diagnostics.cmd.Constants.CMD_RESTART;

/**
 * @author Dariusz.Glugla
 */
@Parameters(commandNames = CMD_RESTART, commandDescription = "restarts the Dynatrace Server (or Collector if specified)")
public class Restart extends AbstractHostPortCommand {

	@Override public void run(CmdOptions options) throws ServerConnectionException, ServerResponseException {
		new ServerManagement(getClient()).restart();
	}
}
