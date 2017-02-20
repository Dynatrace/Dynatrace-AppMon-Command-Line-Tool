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

	public static final String HEADER = "Enable System Profile";

	@Override public void runInternal(CmdOptions options) throws Exception {
		String systemProfileName = getSystemProfile();
		new SystemProfiles(getClient()).enableProfile(systemProfileName);

		out.println(" System Profile '" + systemProfileName + "' is now enabled");
	}

	@Override protected String getHeader() {
		return HEADER;
	}
}
