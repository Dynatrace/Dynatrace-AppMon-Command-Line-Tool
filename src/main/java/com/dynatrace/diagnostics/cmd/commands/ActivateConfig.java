package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.dynatrace.diagnostics.cmd.model.CmdOptions;
import com.dynatrace.sdk.server.systemprofiles.SystemProfiles;

import static com.dynatrace.diagnostics.cmd.Constants.*;
import static java.lang.System.out;

/**
 * @author Dariusz.Glugla
 */
@Parameters(commandNames = CMD_ACTIVECONF, commandDescription = "activates specified configuration")
public class ActivateConfig extends AbstractSystemProfileCommand {

	public static final String HEADER = "Set Active Configuration";

	@Parameter(names = { OPT_CONFIG_SHORT, OPT_CONFIG }, description = "configuration to activate", required = true)
	private String config;

	@Override public void runInternal(CmdOptions options) throws Exception {
		String systemProfileName = getSystemProfile();

		out.print(" setting active configuration to '");
		out.print(config);
		out.print("' for profile '");
		out.print(systemProfileName);
		out.print('\'');

		new SystemProfiles(getClient()).activateProfileConfiguration(systemProfileName, config);

		out.println(": done.");
	}

	@Override protected String getHeader() {
		return HEADER;
	}

	public String getConfig() {
		return config;
	}
}
