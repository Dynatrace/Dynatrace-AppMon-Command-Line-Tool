package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.dynatrace.diagnostics.cmd.model.CmdOptions;
import com.dynatrace.diagnostics.cmd.model.RecordingOption;
import com.dynatrace.sdk.server.sessions.Sessions;
import com.dynatrace.sdk.server.sessions.models.StartRecordingRequest;
import org.apache.commons.lang3.StringUtils;

import static com.dynatrace.diagnostics.cmd.Constants.*;
import static java.lang.System.out;

/**
 * @author Dariusz.Glugla
 */
@Parameters(commandNames = CMD_STARTSESSION,
		commandDescription = "record a new session, using optional sessionname (max. " + SESSION_NAME_MAX_SIZE + " chars)")
public class StartSession extends AbstractSystemProfileCommand {

	public static final String HEADER = "Start Session";

	@Parameter(names = OPT_STARTSESSION_NAME, description = "session name")
	private String sessionName;
	@Parameter(names = FLAG_LOCK, description = "prevents the session from being deleted on cleanups triggered by low disk space")
	private Boolean lock;
	@Parameter(names = FLAG_NOTIMESTAMP, description = "specifies to not append timestamp to sessionname")
	private Boolean noTimestamp;
	@Parameter(names = OPT_RECORDINGOPTION, description = "recording option of the session file", converter = RecordingOption.Converter.class)
	private RecordingOption recordingOption = RecordingOption.ALL;

	@Override public void runInternal(CmdOptions options) throws Exception {
		Sessions sessions = new Sessions(getClient());
		String systemProfileName = getSystemProfile();
		String sessionName = sessions.startRecording(buildRequest(systemProfileName));
		if (StringUtils.isBlank(sessionName)) {
			out.println(" Starting session recording failed.");
		} else {
			out.print(" Session recording successfully started: ");
			out.println(sessionName);
		}
	}

	@Override protected String getHeader() {
		return HEADER;
	}

	private StartRecordingRequest buildRequest(String systemProfile) {
		StartRecordingRequest request = new StartRecordingRequest(systemProfile);
		if (StringUtils.isNotBlank(sessionName)) {
			request.setPresentableName(StringUtils.left(sessionName.trim(), SESSION_NAME_MAX_SIZE));
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

	public String getSessionName() {
		return sessionName;
	}

	public Boolean getLock() {
		return lock;
	}

	public Boolean getNoTimestamp() {
		return noTimestamp;
	}

	public RecordingOption getRecordingOption() {
		return recordingOption;
	}
}
