package com.dynatrace.diagnostics.cmd.model;

import com.beust.jcommander.Parameter;

import static com.dynatrace.diagnostics.cmd.Constants.OPT_DTHOME;

/**
 * @author Dariusz.Glugla
 */
public class CmdOptions {

	@Parameter(names = OPT_DTHOME, description = "Dynatrace home installation directory")
	private String dtHome;

	public String getDtHome() {
		return dtHome;
	}
}
