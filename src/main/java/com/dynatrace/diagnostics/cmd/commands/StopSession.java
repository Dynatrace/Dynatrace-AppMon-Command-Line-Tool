package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameters;
import com.dynatrace.diagnostics.cmd.model.CmdOptions;
import com.dynatrace.sdk.server.sessions.Sessions;
import org.apache.commons.lang3.StringUtils;

import static com.dynatrace.diagnostics.cmd.Constants.CMD_STOPSESSION;
import static java.lang.System.out;

/**
 * @author Dariusz.Glugla
 */
@Parameters(commandNames = CMD_STOPSESSION, commandDescription = "stop recording session")
public class StopSession extends AbstractSystemProfileCommand {

	@Override public void run(CmdOptions options) throws Exception {
		out.println(" ---- Stop Session ----");

		Sessions sessions = new Sessions(getClient());
		String systemProfileName = getSystemProfile();
		String sessionName = sessions.stopRecording(systemProfileName);
		if (StringUtils.isBlank(sessionName)) {
			out.println(" Stopping session recording failed.");
		} else {
			out.print(" Session recording successfully stopped: ");
			out.println(sessionName);
		}
	}
}
