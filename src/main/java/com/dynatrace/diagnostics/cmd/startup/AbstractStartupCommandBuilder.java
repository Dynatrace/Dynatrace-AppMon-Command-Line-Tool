package com.dynatrace.diagnostics.cmd.startup;

import java.io.File;

import static java.lang.System.out;


/**
 * @author Dariusz.Glugla
 * @author cwat-hploch
 */
abstract public class AbstractStartupCommandBuilder {

	private File serverLaunchDir;

	AbstractStartupCommandBuilder(File serverLaunchDir) {
		super();
		this.serverLaunchDir = serverLaunchDir;
	}

	abstract public String buildServerStartupCommand();

	abstract public String buildFrontendServerStartupCommand();

	final String buildCommand(String binary, String devBinary, String startupCommand) {
		File startupFile = new File(serverLaunchDir, binary);
		if (startupFile.exists()) {
			out.print(" Found server startup file: ");
			out.println(startupFile.getAbsolutePath());

			String command = buildCommand(startupFile, startupCommand);

			out.print(" command: ");
			out.println(command);
			return command;
		}
		File startupDevFile = new File(serverLaunchDir, devBinary);
		if (startupDevFile.exists()) {
			out.print(" Found development server startup file: ");
			out.println(startupDevFile.getAbsolutePath());

			String command = startupDevFile.getAbsolutePath();

			out.print(" command: ");
			out.println(command);
			return command;
		}
		out.print(" Could not find server startup file: ");
		out.println(startupFile.getAbsolutePath());
		return null;
	}

	abstract protected String buildCommand(File startupFile, String startupCommand);
}

