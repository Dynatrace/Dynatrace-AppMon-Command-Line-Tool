package com.dynatrace.diagnostics.cmd;

import com.dynatrace.diagnostics.cmd.commands.ActivateConfig;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static com.dynatrace.diagnostics.cmd.Constants.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Dariusz.Glugla
 */
@RunWith(Parameterized.class)
public class Cmd_InvalidParamsTest extends AbstractCmdTest {

	@Parameterized.Parameters
	public static Collection<Object[]> testCases() {
		return Arrays.asList(new Object[][] {
				{ merge(CMD_ACTIVECONF) },
				{ merge(CMD_ACTIVECONF, OPT_CONFIG) },
				{ merge(CMD_ACTIVECONF, OPT_CONFIG, "config") },
				{ merge(CMD_ACTIVECONF, OPT_CONFIG, "config", OPT_SYSTEM_PROFILE) },
				{ merge(CMD_ACTIVECONF, OPT_CONFIG, "config", OPT_SYSTEM_PROFILE, "profile", FLAG_LOCK) },
				{ merge(CMD_DISABLE) },
				{ merge(CMD_DISABLE, OPT_SYSTEM_PROFILE) },
				{ merge(CMD_DISABLE, OPT_SYSTEM_PROFILE, "profile", FLAG_LOCK) },
				{ merge(CMD_ENABLE) },
				{ merge(CMD_ENABLE, OPT_SYSTEM_PROFILE) },
				{ merge(CMD_ENABLE, OPT_SYSTEM_PROFILE, "profile", FLAG_LOCK) },
				{ merge(CMD_LIST_PROFILES, FLAG_LOCK) },
				{ merge(CMD_RESTART, FLAG_LOCK) },
				{ merge(CMD_SHUTDOWN, FLAG_LOCK) },
				{ merge(CMD_STARTSESSION, OPT_STARTSESSION_NAME) },
				{ merge(CMD_STARTSESSION, OPT_SYSTEM_PROFILE) },
				{ merge(CMD_STARTSESSION, OPT_SYSTEM_PROFILE, "profile", OPT_STARTSESSION_NAME, "session", OPT_RECORDINGOPTION) },
				{ merge(CMD_STARTSESSION, OPT_SYSTEM_PROFILE, "profile", OPT_STARTSESSION_NAME, "session", OPT_RECORDINGOPTION,
						"invalidRecOption")
				},
				{ merge(CMD_STARTUP, FLAG_LOCK) },
				{ merge(CMD_STOPSESSION) },
				{ merge(CMD_STOPSESSION, OPT_SYSTEM_PROFILE) }
		});
	}

	public Cmd_InvalidParamsTest(String arguments) {
		super(arguments);
	}

	@Test
	public void test() {
		String out = run();

		assertFalse("Unexpected message for arguments: '" + arguments + "'. Output:\n" + out,
				StringUtils.contains(out, ActivateConfig.HEADER));
		assertTrue("Missing usage for arguments: '" + arguments + "'. Output:\n" + out,
				StringUtils.contains(out, USAGE_PREFIX));
	}
}
