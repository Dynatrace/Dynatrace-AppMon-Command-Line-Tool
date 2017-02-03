package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameters;
import com.dynatrace.diagnostics.cmd.MessagePrinter;
import com.dynatrace.diagnostics.cmd.model.CmdOptions;
import com.dynatrace.sdk.server.systemprofiles.SystemProfiles;

import static com.dynatrace.diagnostics.cmd.Constants.CMD_DISABLE;

/**
 * @author Dariusz.Glugla
 */
@Parameters(commandNames = CMD_DISABLE, commandDescription = "disables the system profile")
public class Disable extends AbstractSystemProfileCommand {

	@Override public void run(CmdOptions options) throws Exception {
		SystemProfiles profiles = new SystemProfiles(getClient());
		String systemProfileName = getSystemProfile();
		profiles.disableProfile(systemProfileName);
		MessagePrinter.printlnSuccessMessage(" ---- System Profile " + systemProfileName + " is now disabled ----");

	}
}
