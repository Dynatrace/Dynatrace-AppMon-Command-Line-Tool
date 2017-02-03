package com.dynatrace.diagnostics.cmd;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.ParametersDelegate;
import com.dynatrace.diagnostics.cmd.commands.*;
import com.dynatrace.diagnostics.cmd.commands.Shutdown;
import com.dynatrace.diagnostics.cmd.model.CmdOptions;
import com.dynatrace.diagnostics.cmd.model.Factory;
import com.dynatrace.sdk.server.BasicServerConfiguration;
import com.dynatrace.sdk.server.DynatraceClient;
import com.dynatrace.sdk.server.ServerConfiguration;
import com.dynatrace.sdk.server.exceptions.ServerConnectionException;
import com.dynatrace.sdk.server.exceptions.ServerResponseException;
import com.dynatrace.sdk.server.servermanagement.ServerManagement;
import org.apache.commons.lang3.StringUtils;

import java.security.InvalidParameterException;

import static com.dynatrace.diagnostics.cmd.Constants.*;
import static com.dynatrace.diagnostics.cmd.MessagePrinter.println;
import static com.dynatrace.diagnostics.cmd.MessagePrinter.printlnErrorMessage;

/**
 * Command Line Tool
 * Features: see method CmdHelper.getUsage()
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

	public Cmd() {
		jCommander = new JCommander(this);

		jCommander.addConverterFactory(new Factory());
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
			println("\n" +
					StringUtils.repeat('-', TITLE_SIZE) + "\n" +
					StringUtils.center(Cmd.class.getPackage().getImplementationTitle(), TITLE_SIZE) + "\n" +
					StringUtils.center("Copyright (C) 2004-2017 " + Cmd.class.getPackage().getImplementationVendor(), TITLE_SIZE)
					+ "\n" +
					StringUtils.repeat('-', TITLE_SIZE) + "\n" +
					StringUtils.center(" Version " + Cmd.class.getPackage().getImplementationVersion(), TITLE_SIZE) + "\n");

			jCommander.parse(args);
			run(getExecutedCommand());
		} catch (ParameterException | InvalidParameterException | NullPointerException e) {
			printlnErrorMessage(e.getMessage() + "\n");
			jCommander.usage();
		}
	}

	private void run(AbstractCommand cmd) {
		try {
			cmd.run(cmdOptions);
		} catch (ServerConnectionException e) {
			printlnErrorMessage("Unable to establish connection with Dynatrace Server: " + e.getMessage());
		} catch (ServerResponseException e) {
			printlnErrorMessage("Unable to fetch response from Dynatrace Server: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			printlnErrorMessage("Uknown error occured: " + e.getMessage());
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

	private void startServer(String userName, String password)
			throws ServerConnectionException, ServerResponseException {
		new ServerManagement(getClient(getBasicServerConfig(userName, password))).restart();
	}

	private DynatraceClient getClient(ServerConfiguration serverConfiguration) {
		return new DynatraceClient(serverConfiguration);
	}

	private ServerConfiguration getBasicServerConfig(String userName, String password) {
		return new BasicServerConfiguration(userName, password);
	}

}
