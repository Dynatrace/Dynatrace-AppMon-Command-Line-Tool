package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameter;

import static com.dynatrace.diagnostics.cmd.Constants.OPT_SYSTEM_PROFILE;

/**
 * @author Dariusz.Glugla
 */
abstract class AbstractSystemProfileCommand extends AbstractHostPortCommand {

	@Parameter(names = OPT_SYSTEM_PROFILE, description = "specifies the system profile to work on", required = true)
	private String systemProfile;

	public String getSystemProfile() {
		return systemProfile;
	}
}
