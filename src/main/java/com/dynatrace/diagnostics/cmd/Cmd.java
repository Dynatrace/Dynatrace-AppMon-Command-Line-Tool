package com.dynatrace.diagnostics.cmd;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.ParametersDelegate;
import com.dynatrace.diagnostics.cmd.commands.*;
import com.dynatrace.diagnostics.cmd.commands.Shutdown;
import com.dynatrace.diagnostics.cmd.model.CmdOptions;
import com.dynatrace.sdk.server.exceptions.ServerConnectionException;
import com.dynatrace.sdk.server.exceptions.ServerResponseException;
import org.apache.commons.lang3.StringUtils;

import java.security.InvalidParameterException;

import static com.dynatrace.diagnostics.cmd.Constants.*;
import static java.lang.System.out;

/**
 * Command Line Tool
 *
 * @author Dariusz.Glugla
 */
public class Cmd {

	private final JCommander jCommander;

	private final AbstractCommand startupCmd = new Startup();
	private final AbstractCommand restartCmd = new Restart();
	private final AbstractCommand shutdownCmd = new Shutdown();
	private final AbstractCommand listProfilesCmd = new ListProfiles();
	private final AbstractCommand enableCmd = new Enable();
	private final AbstractCommand disableCmd = new Disable();
	private final AbstractCommand activateConfigCmd = new ActivateConfig();
	private final AbstractCommand startSessionCmd = new StartSession();
	private final AbstractCommand stopSessionCmd = new StopSession();

	@ParametersDelegate
	private CmdOptions cmdOptions = new CmdOptions();

	private Cmd() {
		jCommander = new JCommander(this);

		jCommander.setAcceptUnknownOptions(true);
		jCommander.setCaseSensitiveOptions(false);
		jCommander.setProgramName(DT_CMD);

		jCommander.addCommand(CMD_STARTUP, startupCmd);
		jCommander.addCommand(CMD_RESTART, restartCmd);
		jCommander.addCommand(CMD_SHUTDOWN, shutdownCmd);
		jCommander.addCommand(CMD_LIST_PROFILES, listProfilesCmd);
		jCommander.addCommand(CMD_ENABLE, enableCmd);
		jCommander.addCommand(CMD_DISABLE, disableCmd);
		jCommander.addCommand(CMD_ACTIVECONF, activateConfigCmd);
		jCommander.addCommand(CMD_STARTSESSION, startSessionCmd);
		jCommander.addCommand(CMD_STOPSESSION, stopSessionCmd);
	}

	public static void main(String[] args) {
		new Cmd().run(args);
	}

	private void run(String[] args) {
		try {
			Package classPackage = Cmd.class.getPackage();
			out.println("\n" +
					StringUtils.repeat('-', TITLE_SIZE) + "\n" +
					StringUtils.center(classPackage.getImplementationTitle(), TITLE_SIZE) + "\n" +
					StringUtils.center("Copyright (C) 2004-2017 " + classPackage.getImplementationVendor(), TITLE_SIZE)
					+ "\n" +
					StringUtils.repeat('-', TITLE_SIZE) + "\n" +
					StringUtils.center(" Version " + classPackage.getImplementationVersion(), TITLE_SIZE) + "\n");

			jCommander.parse(args);
			run(getExecutedCommand());
		} catch (ParameterException | InvalidParameterException | NullPointerException e) {
			out.println(e.getMessage() + "\n");
			jCommander.usage();
		}
	}

	private void run(AbstractCommand cmd) {
		try {
			cmd.run(cmdOptions);
		} catch (ServerConnectionException e) {
			out.println(" Unable to establish connection with Dynatrace Server: " + e.getMessage());
		} catch (ServerResponseException e) {
			out.println(" Unable to fetch response from Dynatrace Server: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			out.println(" Uknown error occured: " + e.getMessage());
		}
	}

	private AbstractCommand getExecutedCommand() {
		String parsedCommand = jCommander.getParsedCommand();
		if (parsedCommand == null) {
			throw new InvalidParameterException("No command specified");
		}
		switch (parsedCommand) {
			case CMD_STARTUP:
				return startupCmd;
			case CMD_RESTART:
				return restartCmd;
			case CMD_SHUTDOWN:
				return shutdownCmd;
			case CMD_LIST_PROFILES:
				return listProfilesCmd;
			case CMD_ENABLE:
				return enableCmd;
			case CMD_DISABLE:
				return disableCmd;
			case CMD_ACTIVECONF:
				return activateConfigCmd;
			case CMD_STARTSESSION:
				return startSessionCmd;
			case CMD_STOPSESSION:
				return stopSessionCmd;
			default:
				jCommander.usage();
				throw new InvalidParameterException("Unknown operation: " + parsedCommand);
		}
	}
}
