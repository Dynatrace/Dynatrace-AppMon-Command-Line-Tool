/***************************************************
 * dynaTrace Diagnostics (c) dynaTrace software GmbH
 *
 * @file: AbstractStartupCommandBuilder.java
 * @date: 24.04.2014
 * @author: cwat-hploch
 */
package com.dynatrace.diagnostics.cmd.startup;

import java.io.File;


/**
 * @author Dariusz.Glugla
 * @author cwat-hploch
 */
abstract public class AbstractStartupCommandBuilder {

	protected StringBuilder sb;
	protected File serverLaunchDir;

	public AbstractStartupCommandBuilder(StringBuilder sb, File serverLaunchDir) {
		super();
		this.sb = sb;
		this.serverLaunchDir = serverLaunchDir;
	}

	abstract public String buildServerStartupCommand();

	abstract public String buildFrontendServerStartupCommand();

	protected final String buildCommand(String binary, String devBinary, String startupCommand) {
		File startupFile = new File(serverLaunchDir, binary);
		if (startupFile.exists()) {
			sb.append(" Found server startup file: ").append(startupFile.getAbsolutePath()).append('\n');

			String command = buildCommand(startupFile, startupCommand);

			sb.append(" command: ").append(command).append('\n');
			return command;
		}
		File startupDevFile = new File(serverLaunchDir, devBinary);
		if (startupDevFile.exists()) {
			sb.append(" Found development server startup file: ").append(startupDevFile.getAbsolutePath()).append('\n');

			String command = startupDevFile.getAbsolutePath();

			sb.append(" command: ").append(command).append('\n');
			return command;
		}
		sb.append(" Could not find server startup file: ").append(startupFile.getAbsolutePath()).append('\n');
		return null;
	}

	abstract protected String buildCommand(File startupFile, String startupCommand);
}

