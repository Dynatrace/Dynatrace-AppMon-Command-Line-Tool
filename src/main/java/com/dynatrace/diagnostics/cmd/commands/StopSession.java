package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameters;
import com.dynatrace.diagnostics.cmd.model.CmdOptions;
import com.dynatrace.sdk.server.sessions.Sessions;
import org.apache.commons.lang3.StringUtils;

import static com.dynatrace.diagnostics.cmd.Constants.CMD_STOPSESSION;
import static com.dynatrace.diagnostics.cmd.MessagePrinter.printlnErrorMessage;
import static com.dynatrace.diagnostics.cmd.MessagePrinter.printlnSuccessMessage;

/**
 * @author Dariusz.Glugla
 */
@Parameters(commandNames = CMD_STOPSESSION, commandDescription = "stop recording session")
public class StopSession extends AbstractSystemProfileCommand {

	@Override public void run(CmdOptions options) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" ---- Stop Session ----\n");

		Sessions sessions = new Sessions(getClient());
		String systemProfileName = getSystemProfile();
		String sessionName = sessions.stopRecording(systemProfileName);
		if (StringUtils.isBlank(sessionName)) {
			sb.append(" Stopping session recording failed.\n");
			printlnErrorMessage(sb.toString());
		} else {
			sb.append(" Session recording successfully stopped: ").append(sessionName).append('\n');
			printlnSuccessMessage(sb.toString());
		}
	}
}
