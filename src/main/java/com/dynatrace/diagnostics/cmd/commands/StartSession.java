package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.dynatrace.diagnostics.cmd.model.CmdOptions;
import com.dynatrace.diagnostics.cmd.model.RecordingOption;
import com.dynatrace.sdk.server.sessions.Sessions;
import com.dynatrace.sdk.server.sessions.models.StartRecordingRequest;
import org.apache.commons.lang3.StringUtils;

import static com.dynatrace.diagnostics.cmd.Constants.*;
import static com.dynatrace.diagnostics.cmd.MessagePrinter.printlnErrorMessage;
import static com.dynatrace.diagnostics.cmd.MessagePrinter.printlnSuccessMessage;

/**
 * @author Dariusz.Glugla
 */
@Parameters(commandNames = CMD_STARTSESSION,
		commandDescription = "record a new session, using optional sessionname (max. " + SESSION_NAME_MAX_SIZE + " chars)")
public class StartSession extends AbstractSystemProfileCommand {

	@Parameter(names = OPT_STARTSESSION_NAME, description = "session name")
	private String sessionName;
	@Parameter(names = FLAG_LOCK, description = "prevents the session from being deleted on cleanups triggered by low disk space")
	private Boolean lock;
	@Parameter(names = FLAG_NOTIMESTAMP, description = "specifies to not append timestamp to sessionname")
	private Boolean noTimestamp;
	@Parameter(names = OPT_RECORDINGOPTION, description = "recording option of the session file", required = true)
	private RecordingOption recordingOption = RecordingOption.ALL;

	@Override public void run(CmdOptions options) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" ---- Start Session ----\n");

		Sessions sessions = new Sessions(getClient());
		String systemProfileName = getSystemProfile();
		String sessionName = sessions.startRecording(buildRequest(systemProfileName));
		if (StringUtils.isBlank(sessionName)) {
			sb.append(" Starting session recording failed.\n");
			printlnErrorMessage(sb.toString());
		} else {
			sb.append(" Session recording successfully started: ").append(sessionName).append('\n');
			printlnSuccessMessage(sb.toString());
		}
	}

	private StartRecordingRequest buildRequest(String systemProfile) {
		StartRecordingRequest request = new StartRecordingRequest(systemProfile);
		if (StringUtils.isNotBlank(sessionName)) {
			request.setPresentableName(sessionName);
		}
		if (noTimestamp != null) {
			request.setTimestampAllowed(!noTimestamp);
		}
		request.setRecordingOption(recordingOption.toSDK());
		if (lock != null) {
			request.setSessionLocked(lock);
		}
		return request;
	}
}
