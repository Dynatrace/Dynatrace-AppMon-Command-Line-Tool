package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameters;
import com.dynatrace.diagnostics.cmd.MessagePrinter;
import com.dynatrace.diagnostics.cmd.model.CmdOptions;
import com.dynatrace.sdk.server.systemprofiles.SystemProfiles;
import com.dynatrace.sdk.server.systemprofiles.models.SystemProfile;

import static com.dynatrace.diagnostics.cmd.Constants.CMD_LIST_PROFILES;

/**
 * @author Dariusz.Glugla
 */
@Parameters(commandNames = CMD_LIST_PROFILES, commandDescription = "display system profiles")
public class ListProfiles extends AbstractHostPortCommand {

	@Override
	public void run(CmdOptions options) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" ---- List System Profiles ----\n");

		SystemProfiles systemProfiles = new SystemProfiles(getClient());
		int counter = 1;
		for (SystemProfile systemProfile : systemProfiles.getSystemProfiles().getProfiles()) {
			sb.append(' ').append((counter++)).append(":\t").append(systemProfile.getId()).append('\n');
		}
		MessagePrinter.printlnSuccessMessage(sb.toString());
	}

}
