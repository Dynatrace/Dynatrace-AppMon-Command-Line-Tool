package com.dynatrace.diagnostics.cmd;

import org.apache.commons.lang3.StringUtils;

/**
 * Constants definitions for CMD
 *
 * @author Dariusz.Glugla
 * @author helmut.hackl anita.engleder
 */
public final class Constants {

	private Constants() {
		// prevent from creation
	}

	public static final String DT_HOME = "DT_HOME";

	public static final String TITLE_PREFIX = StringUtils.repeat('-', 4);
	static final int TITLE_SIZE = 60;

	// max size in repository db for session name is 100
	// we need to set it to 80, because date is added on serverside
	public static final int SESSION_NAME_MAX_SIZE = 80;

	//Hidden dtCmd options
	public static final String OPT_DTHOME = "-dthome";

	static final String DT_CMD = "dtcmd";

	static final String MSG_NO_COMMAND_SPECIFIED = "No command specified";
	static final String MSG_UNKNOWN_OPERATION = "Unknown operation: ";
	static final String MSG_NO_CONNECTION = " Unable to establish connection with Dynatrace Server: ";
	static final String MSG_NO_RESPONSE = " Unable to fetch response from Dynatrace Server: ";
	static final String MSG_UNKNOWN_ERROR = " Uknown error occured: ";

	public static final String FLAG_VERBOSE = "-verbose";
	public static final String FLAG_NO_SSL = "-nossl";
	public static final String FLAG_LOCK = "-lock";
	public static final String FLAG_NOTIMESTAMP = "-notimestamp";

	public static final String CMD_STARTUP = "-startup";
	public static final String CMD_RESTART = "-restart";
	public static final String CMD_SHUTDOWN = "-shutdown";
	public static final String CMD_LIST_PROFILES = "-listprofiles";
	public static final String CMD_ENABLE = "-enable";
	public static final String CMD_DISABLE = "-disable";
	public static final String CMD_ACTIVECONF = "-activeconf";
	public static final String CMD_STARTSESSION = "-startsession";
	public static final String CMD_STOPSESSION = "-stopsession";

	public static final String OPT_USER = "-user";
	public static final String OPT_PASS = "-pass";
	public static final String OPT_SYSTEM_PROFILE = "-profile";
	public static final String OPT_RECORDINGOPTION = "-recordingoption";
	public static final String OPT_CONFIG_SHORT = "-c";
	public static final String OPT_CONFIG = "-config";
	public static final String OPT_STARTSESSION_NAME = "-f";

}
