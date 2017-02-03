package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameter;
import com.dynatrace.diagnostics.cmd.model.CmdOptions;

import static com.dynatrace.diagnostics.cmd.Constants.FLAG_VERBOSE;

/**
 * @author Dariusz.Glugla
 */
public abstract class AbstractCommand {

	@Parameter(names = FLAG_VERBOSE, description = "provide a more detailed output (in case of errors)")
	private Boolean verbose;

	public abstract void run(CmdOptions options) throws Exception;

	public Boolean getVerbose() {
		return verbose;
	}
}
