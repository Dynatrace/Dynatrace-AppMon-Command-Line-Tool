package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.dynatrace.diagnostics.cmd.Constants;

/**
 * @author Dariusz.Glugla
 */
@Parameters(commandDescription = "display server statistics")
public class SystemProfile {

	@Parameter(names = Constants.FLAG_STAT_BRIEF, description = "shorten to brief statistics")
	private Boolean brief;
}
