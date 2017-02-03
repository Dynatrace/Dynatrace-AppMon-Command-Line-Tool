package com.dynatrace.diagnostics.cmd.startup;

import java.io.File;


/**
 * @author Dariusz.Glugla
 * @author cwat-hploch
 */
public class LinuxStartupCommandBuilder extends AbstractStartupCommandBuilder {

	public static final String SERVER_BINARY = "/init.d/dynaTraceServer";
	private static final String FRONTEND_SERVER_BINARY = "/init.d/dynaTraceFrontendServer";
	private static final String SERVER_DEV_BINARY = "dtserver";
	private static final String FRONTEND_SERVER_DEV_BINARY = "dtfrontendserver";
	private static final String SERVER_SERVICE_NAME = "start-backend";
	private static final String FRONTEND_SERVER_SERVICE_NAME = "start-frontend";


	public LinuxStartupCommandBuilder(StringBuilder sb, File serverLaunchDir) {
		super(sb, serverLaunchDir);
	}

	@Override
	public String buildServerStartupCommand() {
		return buildCommand(SERVER_BINARY, SERVER_DEV_BINARY, SERVER_SERVICE_NAME);
	}

	@Override
	public String buildFrontendServerStartupCommand() {
		return buildCommand(FRONTEND_SERVER_BINARY, FRONTEND_SERVER_DEV_BINARY, FRONTEND_SERVER_SERVICE_NAME);
	}

	@Override protected String buildCommand(File startupFile, String startupCommand) {
		return startupFile.getAbsolutePath() + " " + startupCommand;
	}

}
