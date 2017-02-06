package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameters;
import com.dynatrace.diagnostics.cmd.model.CmdOptions;
import com.dynatrace.diagnostics.cmd.startup.AbstractStartupCommandBuilder;
import com.dynatrace.diagnostics.cmd.startup.LinuxStartupCommandBuilder;
import com.dynatrace.diagnostics.cmd.startup.StartupCommandBuilderFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

import static com.dynatrace.diagnostics.cmd.Constants.CMD_STARTUP;
import static com.dynatrace.diagnostics.cmd.Constants.DT_HOME;
import static java.lang.System.out;

/**
 * @author Dariusz.Glugla
 */
@Parameters(commandNames = CMD_STARTUP, commandDescription = "starts up local server")
public class Startup extends AbstractCommand {

	private static final String LAUNCHER_INI_FILENAME = "dtserver.ini";

	@Override
	public void run(CmdOptions options) {
		out.println(" ---- Startup ----");
		out.println(" starting server ...");

		String osName = System.getProperty("os.name").toUpperCase();
		boolean isWindows = osName.startsWith("WIN");
		boolean isLinux = osName.startsWith("LINUX");
		// see http://mindprod.com/jgloss/properties.html#OSNAME for OS-name property

		File serverLaunchDir = searchServerLaunchDir(isWindows || isLinux, options);
		if (serverLaunchDir == null) {
			out.println(" Unable to find the server launcher. Please set the " + DT_HOME + " environment variable.");
			return;
		}

		String errorMessage = startupOSDependentServers(serverLaunchDir, isWindows);
		if (errorMessage == null) {
			return;
		}

		out.println(" Unable to start server.");
		out.print(" Failed with: ");
		out.print(errorMessage);
		out.println(".");
		if (isWindows) {
			out.println(" Please ensure to run dtcmd as administrator!");
		}
	}

	private String startupOSDependentServers(File serverLaunchDir, boolean isWindows) {
		AbstractStartupCommandBuilder commandBuilder = StartupCommandBuilderFactory.startupCommandBuilder(
				isWindows ? StartupCommandBuilderFactory.OS.Windows : StartupCommandBuilderFactory.OS.Linux, serverLaunchDir);
		if (commandBuilder == null) {
			return null;
		}

		return startUpServers(serverLaunchDir,
				commandBuilder.buildServerStartupCommand(),
				commandBuilder.buildFrontendServerStartupCommand());
	}

	private String startUpServers(File serverLaunchDir, String commandServer, String commandFrontendServer) {
		ProcessStarter processStarter = new ProcessStarter();
		if (StringUtils.isNotBlank(commandServer) && !processStarter.run(commandServer, serverLaunchDir)) {
			return processStarter.getErrorMessage();
		}

		if (StringUtils.isNotBlank(commandFrontendServer) && !processStarter.run(commandFrontendServer, serverLaunchDir)) {
			return processStarter.getErrorMessage();
		}

		return null;
	}

	/**
	 * Searches Directory containing ServerLaunchIniFile.
	 *
	 * @return ServerLaunchIniFile or null if not found
	 * @author anita.engleder
	 */
	private File searchServerLaunchDir(boolean lookForIniFile, CmdOptions options) {
		// 1. dtHome can be forced via dtAnt ("hidden" flag):
		if (StringUtils.isNotBlank(options.getDtHome())) {
			File path = new File(options.getDtHome());
			if (containsLaunchFiles(path, lookForIniFile)) {
				return path;
			}
		} else {
			// 2. DT_HOME can be forced via environment variable
			String dtEnvHome = null;
			boolean isUnableToReceiveENVHOME = false;
			try {
				dtEnvHome = System.getenv(DT_HOME);
			} catch (Error e) {
				out.print(" Warning: Please ensure that ");
				out.print(DT_HOME);
				out.println(" system property is set correctly.");
			} catch (SecurityException e) {
				out.print(" Unable to receive the ");
				out.print(DT_HOME);
				out.println(" environment variable. Insufficient privileges.");
				isUnableToReceiveENVHOME = true;
			}
			try {
				if (dtEnvHome == null) {
					String dtEnvHomeSysProp = System.getProperty(DT_HOME);
					if (StringUtils.isNotBlank(dtEnvHomeSysProp)) {
						dtEnvHome = dtEnvHomeSysProp.trim();
					}
				}
			} catch (SecurityException e) {
				if (!isUnableToReceiveENVHOME) {
					out.print(" Unable to receive the ");
					out.print(DT_HOME);
					out.println(" system property. Insufficient privileges.");
				}
			}

			if (dtEnvHome != null) {
				File dtEnvHomePath = new File(dtEnvHome);
				if (containsLaunchFiles(dtEnvHomePath, lookForIniFile)) {
					return dtEnvHomePath;
				}
			}

			// 3. Fallback to look in current directory:
			File path = new File(".");
			if (containsLaunchFiles(path, lookForIniFile)) {
				return path;
			}

			// 4. Fallback to look in "..\\jloadtrace\\":
			path = new File("..\\jloadtrace\\");
			if (containsLaunchFiles(path, lookForIniFile)) {
				return path;
			}

		}
		return null;
	}

	/**
	 * Checks if the startup script or the launcher ini file exists in the given path.
	 *
	 * @author anita.engleder
	 */
	private boolean containsLaunchFiles(File path, boolean lookForIniFile) {
		if (lookForIniFile) {
			return existsLauncherIniFile(path);
		}
		return existsStartupScript(path);
	}

	/**
	 * Checks if the launcher ini file exists in the given path.
	 *
	 * @author anita.engleder
	 */
	private boolean existsLauncherIniFile(File path) {
		return new File(path, LAUNCHER_INI_FILENAME).exists();
	}

	/**
	 * Checks if the startup script exists in the given path. Necessary for Cross Unix platforms.
	 */
	private boolean existsStartupScript(File path) {
		return new File(path, LinuxStartupCommandBuilder.SERVER_BINARY).exists();
	}

	/**
	 * Utility class to start a process via system command.
	 *
	 * @author martin.wurzinger
	 */
	private class ProcessStarter {

		private String errorMessage;


		/**
		 * @param command    the system command to start the subprocess
		 * @param workingDir the working directory of the subprocess, or null if the subprocess should
		 *                   inherit the working directory of the current process.
		 * @return startup success: <code>true</code> if the process was started successful or
		 * <code>false</code> otherwise
		 * @author martin.wurzinger
		 */
		public boolean run(String command, File workingDir) {
			final Process proc;

			try {
				proc = Runtime.getRuntime().exec(command, null, workingDir);
			} catch (IOException e) {
				errorMessage = e.getMessage();
				return false;
			}

			new StreamRedirector(proc.getInputStream(), out).start();
			new StreamRedirector(proc.getErrorStream(), System.err).start();

			return true;
		}

		String getErrorMessage() {
			return errorMessage;
		}
	}

	/**
	 * @author martin.wurzinger
	 */
	private class StreamRedirector extends Thread {

		private static final int BUFFER_LENGTH = 2048;

		private InputStream in = null;
		private PrintStream out = null;

		StreamRedirector(InputStream in, PrintStream out) {
			this.in = in;
			this.out = out;
		}

		@Override public void run() {
			if (in == null || out == null) {
				return;
			}

			try {
				BufferedInputStream inBuffer = new BufferedInputStream(in);
				int i;
				byte[] buf = new byte[BUFFER_LENGTH];
				while ((i = inBuffer.read(buf, 0, BUFFER_LENGTH)) != -1) {
					out.write(buf, 0, i);
				}
				inBuffer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
