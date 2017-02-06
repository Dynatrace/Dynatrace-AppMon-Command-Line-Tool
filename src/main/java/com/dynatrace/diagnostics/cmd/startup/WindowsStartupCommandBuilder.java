package com.dynatrace.diagnostics.cmd.startup;

import java.io.File;


/**
 * @author Dariusz.Glugla
 * @author cwat-hploch
 */
public class WindowsStartupCommandBuilder extends AbstractStartupCommandBuilder {

	private static final String SERVER_BINARY = "dtserver.exe";
	private static final String FRONTEND_SERVER_BINARY = "dtfrontendserver.exe";
	private static final String SERVER_SERVICE_NAME = "Dynatrace Server";
	private static final String FRONTEND_SERVER_SERVICE_NAME = "Dynatrace Frontend Server";

	WindowsStartupCommandBuilder(File serverLaunchDir) {
		super(serverLaunchDir);
	}

	@Override
	public String buildServerStartupCommand() {
		return buildCommand(SERVER_BINARY, SERVER_BINARY, SERVER_SERVICE_NAME);
	}

	@Override
	public String buildFrontendServerStartupCommand() {
		return buildCommand(FRONTEND_SERVER_BINARY, FRONTEND_SERVER_BINARY, FRONTEND_SERVER_SERVICE_NAME);
	}

	@Override protected String buildCommand(File startupFile, String serviceName) {
		return startupFile.getAbsolutePath() + " -service start -servicename \"" + serviceName + "\"";
	}
}
