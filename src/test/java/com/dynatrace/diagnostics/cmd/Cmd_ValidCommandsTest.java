package com.dynatrace.diagnostics.cmd;

import com.dynatrace.diagnostics.cmd.commands.*;
import com.dynatrace.diagnostics.cmd.commands.Shutdown;
import com.dynatrace.diagnostics.cmd.model.HostPort;
import com.dynatrace.diagnostics.cmd.model.RecordingOption;
import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;

import static com.dynatrace.diagnostics.cmd.Constants.*;
import static org.junit.Assert.*;

/**
 * @author Dariusz.Glugla
 */
public class Cmd_ValidCommandsTest {

	private final String systemProfile = "systemProfile";
	private final String user = "userName";
	private final String password = "password";
	private final String host = "host";
	private final int port = 1234;

	@Test
	public void testActivateConfig() {
		final String configName = "configName";

		AbstractCommand command = new Cmd().getExecutedCommand(new String[] {
				CMD_ACTIVECONF,
				OPT_CONFIG, configName,
				OPT_SYSTEM_PROFILE, systemProfile,
				OPT_USER, user,
				OPT_PASS, password,
				FLAG_NO_SSL,
				"host:1234"
		});
		assertTrue(command instanceof ActivateConfig);

		ActivateConfig activateConfig = (ActivateConfig) command;
		assertEquals(configName, activateConfig.getConfig());
		assertEquals(systemProfile, activateConfig.getSystemProfile());
		assertEquals(user, activateConfig.getUser());
		assertEquals(password, activateConfig.getPassword());
		assertTrue(BooleanUtils.isTrue(activateConfig.getNoSSL()));
		assertEquals(1, activateConfig.getHostPorts().size());
		HostPort hostPort = activateConfig.getHostPorts().get(0);
		assertEquals(host, hostPort.getHost());
		assertEquals(port, hostPort.getPort().intValue());
	}

	@Test
	public void testDisable() {
		AbstractCommand command = new Cmd().getExecutedCommand(new String[] {
				CMD_DISABLE,
				OPT_SYSTEM_PROFILE, systemProfile,
				OPT_USER, user,
				OPT_PASS, password,
				"host:1234"
		});
		assertTrue(command instanceof Disable);

		Disable disable = (Disable) command;
		assertEquals(systemProfile, disable.getSystemProfile());
		assertEquals(user, disable.getUser());
		assertEquals(password, disable.getPassword());
		assertFalse(BooleanUtils.isTrue(disable.getNoSSL()));
		assertEquals(1, disable.getHostPorts().size());
		HostPort hostPort = disable.getHostPorts().get(0);
		assertEquals(host, hostPort.getHost());
		assertEquals(port, hostPort.getPort().intValue());
	}

	@Test
	public void testEnable() {
		AbstractCommand command = new Cmd().getExecutedCommand(new String[] {
				CMD_ENABLE,
				OPT_SYSTEM_PROFILE, systemProfile,
				OPT_USER, user,
				OPT_PASS, password,
				"host:1234"
		});
		assertTrue(command instanceof Enable);

		Enable enable = (Enable) command;
		assertEquals(systemProfile, enable.getSystemProfile());
		assertEquals(user, enable.getUser());
		assertEquals(password, enable.getPassword());
		assertFalse(BooleanUtils.isTrue(enable.getNoSSL()));
		assertEquals(1, enable.getHostPorts().size());
		HostPort hostPort = enable.getHostPorts().get(0);
		assertEquals(host, hostPort.getHost());
		assertEquals(port, hostPort.getPort().intValue());
	}

	@Test
	public void testListProfiles() {
		AbstractCommand command = new Cmd().getExecutedCommand(new String[] {
				CMD_LIST_PROFILES,
				OPT_USER, user,
				OPT_PASS, password,
				FLAG_NO_SSL,
				"host:1234"
		});
		assertTrue(command instanceof ListProfiles);

		ListProfiles listProfiles = (ListProfiles) command;
		assertEquals(user, listProfiles.getUser());
		assertEquals(password, listProfiles.getPassword());
		assertTrue(BooleanUtils.isTrue(listProfiles.getNoSSL()));
		assertEquals(1, listProfiles.getHostPorts().size());
		HostPort hostPort = listProfiles.getHostPorts().get(0);
		assertEquals(host, hostPort.getHost());
		assertEquals(port, hostPort.getPort().intValue());
	}

	@Test
	public void testRestart() {
		AbstractCommand command = new Cmd().getExecutedCommand(new String[] {
				CMD_RESTART,
				OPT_USER, user,
				OPT_PASS, password,
				FLAG_NO_SSL,
				"host:1234"
		});
		assertTrue(command instanceof Restart);

		Restart restart = (Restart) command;
		assertEquals(user, restart.getUser());
		assertEquals(password, restart.getPassword());
		assertTrue(BooleanUtils.isTrue(restart.getNoSSL()));
		assertEquals(1, restart.getHostPorts().size());
		HostPort hostPort = restart.getHostPorts().get(0);
		assertEquals(host, hostPort.getHost());
		assertEquals(port, hostPort.getPort().intValue());
	}

	@Test
	public void testShutdown() {
		AbstractCommand command = new Cmd().getExecutedCommand(new String[] {
				CMD_SHUTDOWN,
				OPT_USER, user,
				OPT_PASS, password,
				FLAG_NO_SSL,
				"host:1234"
		});
		assertTrue(command instanceof Shutdown);

		Shutdown shutdown = (Shutdown) command;
		assertEquals(user, shutdown.getUser());
		assertEquals(password, shutdown.getPassword());
		assertTrue(BooleanUtils.isTrue(shutdown.getNoSSL()));
		assertEquals(1, shutdown.getHostPorts().size());
		HostPort hostPort = shutdown.getHostPorts().get(0);
		assertEquals(host, hostPort.getHost());
		assertEquals(port, hostPort.getPort().intValue());
	}

	@Test
	public void testStartSession() {
		final String sessionName = "sessionName";

		AbstractCommand command = new Cmd().getExecutedCommand(new String[] {
				CMD_STARTSESSION,
				OPT_SYSTEM_PROFILE, systemProfile,
				OPT_STARTSESSION_NAME, sessionName,
				FLAG_LOCK,
				FLAG_NOTIMESTAMP,
				OPT_RECORDINGOPTION, "violations",
				OPT_USER, user,
				OPT_PASS, password,
				FLAG_NO_SSL,
				"host:1234"
		});
		assertTrue(command instanceof StartSession);

		StartSession startSession = (StartSession) command;
		assertEquals(systemProfile, startSession.getSystemProfile());
		assertEquals(sessionName, startSession.getSessionName());
		assertTrue(BooleanUtils.isTrue(startSession.getLock()));
		assertTrue(BooleanUtils.isTrue(startSession.getNoTimestamp()));
		assertEquals(RecordingOption.VIOLATIONS, startSession.getRecordingOption());
		assertEquals(user, startSession.getUser());
		assertEquals(password, startSession.getPassword());
		assertTrue(BooleanUtils.isTrue(startSession.getNoSSL()));
		assertEquals(1, startSession.getHostPorts().size());
		HostPort hostPort = startSession.getHostPorts().get(0);
		assertEquals(host, hostPort.getHost());
		assertEquals(port, hostPort.getPort().intValue());
	}

	@Test
	public void testStartup() {
		AbstractCommand command = new Cmd().getExecutedCommand(new String[] {
				CMD_STARTUP,
				FLAG_VERBOSE
		});
		assertTrue(command instanceof Startup);

		Startup startup = (Startup) command;
		assertTrue(BooleanUtils.isTrue(startup.getVerbose()));
	}

	@Test
	public void testStopSession() {
		final String sessionName = "sessionName";

		AbstractCommand command = new Cmd().getExecutedCommand(new String[] {
				CMD_STOPSESSION,
				OPT_SYSTEM_PROFILE, systemProfile,
				OPT_USER, user,
				OPT_PASS, password,
				FLAG_NO_SSL,
				"host:1234"
		});
		assertTrue(command instanceof StopSession);

		StopSession stopSession = (StopSession) command;
		assertEquals(systemProfile, stopSession.getSystemProfile());
		assertEquals(user, stopSession.getUser());
		assertEquals(password, stopSession.getPassword());
		assertTrue(BooleanUtils.isTrue(stopSession.getNoSSL()));
		assertEquals(1, stopSession.getHostPorts().size());
		HostPort hostPort = stopSession.getHostPorts().get(0);
		assertEquals(host, hostPort.getHost());
		assertEquals(port, hostPort.getPort().intValue());
	}
}
