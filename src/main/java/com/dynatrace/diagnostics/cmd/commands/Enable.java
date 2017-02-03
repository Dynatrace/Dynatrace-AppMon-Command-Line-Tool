package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameters;
import com.dynatrace.diagnostics.cmd.MessagePrinter;
import com.dynatrace.diagnostics.cmd.model.CmdOptions;
import com.dynatrace.sdk.server.systemprofiles.SystemProfiles;

import static com.dynatrace.diagnostics.cmd.Constants.CMD_ENABLE;

/**
 * @author Dariusz.Glugla
 */
@Parameters(commandNames = CMD_ENABLE, commandDescription = "enables the system profile")
public class Enable extends AbstractSystemProfileCommand {

	@Override public void run(CmdOptions options) throws Exception {
		SystemProfiles profiles = new SystemProfiles(getClient());
		String systemProfileName = getSystemProfile();
		profiles.enableProfile(systemProfileName);
		MessagePrinter.printlnSuccessMessage(" ---- System Profile " + systemProfileName + " is now enabled ----");
	}
}
