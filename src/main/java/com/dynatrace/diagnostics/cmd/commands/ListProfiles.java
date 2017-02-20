package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameters;
import com.dynatrace.diagnostics.cmd.model.CmdOptions;
import com.dynatrace.sdk.server.systemprofiles.SystemProfiles;
import com.dynatrace.sdk.server.systemprofiles.models.SystemProfile;

import static com.dynatrace.diagnostics.cmd.Constants.CMD_LIST_PROFILES;
import static java.lang.System.out;

/**
 * @author Dariusz.Glugla
 */
@Parameters(commandNames = CMD_LIST_PROFILES, commandDescription = "display system profiles")
public class ListProfiles extends AbstractHostPortCommand {

	public static final String HEADER = "List System Profiles";

	@Override
	public void runInternal(CmdOptions options) throws Exception {
		SystemProfiles systemProfiles = new SystemProfiles(getClient());
		int counter = 1;
		for (SystemProfile systemProfile : systemProfiles.getSystemProfiles().getProfiles()) {
			out.println(String.format(" %3d", counter++) + ":\t" + systemProfile.getId());
		}
	}

	@Override protected String getHeader() {
		return HEADER;
	}
}
