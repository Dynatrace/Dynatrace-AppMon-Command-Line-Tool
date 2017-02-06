package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameters;
import com.dynatrace.diagnostics.cmd.model.CmdOptions;
import com.dynatrace.sdk.server.systemprofiles.SystemProfiles;

import static com.dynatrace.diagnostics.cmd.Constants.CMD_DISABLE;
import static java.lang.System.out;

/**
 * @author Dariusz.Glugla
 */
@Parameters(commandNames = CMD_DISABLE, commandDescription = "disables the system profile")
public class Disable extends AbstractSystemProfileCommand {

	@Override public void run(CmdOptions options) throws Exception {
		out.println(" ---- Disable System Profile ----");

		String systemProfileName = getSystemProfile();
		new SystemProfiles(getClient()).disableProfile(systemProfileName);

		out.println(" System Profile '" + systemProfileName + "' is now disabled");
	}
}
