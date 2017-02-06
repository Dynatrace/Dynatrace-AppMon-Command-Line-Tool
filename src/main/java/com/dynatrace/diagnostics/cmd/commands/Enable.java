package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameters;
import com.dynatrace.diagnostics.cmd.model.CmdOptions;
import com.dynatrace.sdk.server.systemprofiles.SystemProfiles;

import static com.dynatrace.diagnostics.cmd.Constants.CMD_ENABLE;
import static java.lang.System.out;

/**
 * @author Dariusz.Glugla
 */
@Parameters(commandNames = CMD_ENABLE, commandDescription = "enables the system profile")
public class Enable extends AbstractSystemProfileCommand {

	@Override public void run(CmdOptions options) throws Exception {
		out.println(" ---- Enable System Profile ----");

		String systemProfileName = getSystemProfile();
		new SystemProfiles(getClient()).enableProfile(systemProfileName);

		out.println(" System Profile '" + systemProfileName + "' is now enabled");
	}
}
