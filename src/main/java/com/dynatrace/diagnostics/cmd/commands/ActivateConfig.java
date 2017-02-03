package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.dynatrace.diagnostics.cmd.model.CmdOptions;
import com.dynatrace.sdk.server.systemprofiles.SystemProfiles;

import static com.dynatrace.diagnostics.cmd.Constants.*;
import static com.dynatrace.diagnostics.cmd.MessagePrinter.printlnSuccessMessage;

/**
 * @author Dariusz.Glugla
 */
@Parameters(commandNames = CMD_ACTIVECONF, commandDescription = "activates specified configuration")
public class ActivateConfig extends AbstractSystemProfileCommand {

	@Parameter(names = { OPT_CONFIG_SHORT, OPT_CONFIG }, description = "configuration to activate", required = true)
	private String config;

	public String getConfig() {
		return config;
	}

	@Override public void run(CmdOptions options) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" ---- Set Active Configuration ----\n");

		String configurationName = getConfig();
		String systemProfileName = getSystemProfile();
		new SystemProfiles(getClient()).activateProfileConfiguration(systemProfileName, configurationName);

		sb.append(" setting active configuration to ").append(configurationName).append(" for profile ").append(systemProfileName)
				.append(" done.\n");
		printlnSuccessMessage(sb.toString());
	}
}
