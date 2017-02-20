package com.dynatrace.diagnostics.cmd;

import com.dynatrace.diagnostics.cmd.commands.Restart;
import com.dynatrace.diagnostics.cmd.commands.Shutdown;
import com.dynatrace.diagnostics.cmd.commands.Startup;
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
public class Cmd_MultipleCommandsTest extends AbstractCmdTest {

	@Parameterized.Parameters
	public static Collection<Object[]> testCases() {
		return Arrays.asList(new Object[][] {
				{ merge(FLAG_NO_SSL, CMD_STARTUP, CMD_SHUTDOWN) },
				{ merge(FLAG_NO_SSL, CMD_STARTUP, CMD_RESTART) },
				{ merge(FLAG_NO_SSL, CMD_STARTUP, CMD_SHUTDOWN, CMD_RESTART) },
				{ merge(FLAG_NO_SSL, CMD_SHUTDOWN, CMD_STARTUP) },
				{ merge(FLAG_NO_SSL, CMD_SHUTDOWN, CMD_RESTART) },
				{ merge(FLAG_NO_SSL, CMD_SHUTDOWN, CMD_STARTUP, CMD_RESTART) },
				{ merge(FLAG_NO_SSL, CMD_RESTART, CMD_STARTUP) },
				{ merge(FLAG_NO_SSL, CMD_RESTART, CMD_SHUTDOWN) },
				{ merge(FLAG_NO_SSL, CMD_RESTART, CMD_STARTUP, CMD_SHUTDOWN) }
		});
	}

	public Cmd_MultipleCommandsTest(String arguments) {
		super(arguments);
	}

	@Test
	public void test() {
		String out = run();

		assertFalse("Unexpected message for arguments: '" + arguments + "'. Output:\n" + out,
				StringUtils.contains(out, Startup.HEADER) ||
						StringUtils.contains(out, Shutdown.HEADER) ||
						StringUtils.contains(out, Restart.HEADER));
		assertTrue("Missing usage for arguments: '" + arguments + "'. Output:\n" + out,
				StringUtils.contains(out, USAGE_PREFIX));
	}
}
