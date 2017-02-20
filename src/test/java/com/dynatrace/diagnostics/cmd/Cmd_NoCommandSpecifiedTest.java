package com.dynatrace.diagnostics.cmd;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static com.dynatrace.diagnostics.cmd.Constants.*;
import static org.junit.Assert.assertTrue;

/**
 * @author Dariusz.Glugla
 */
@RunWith(Parameterized.class)
public class Cmd_NoCommandSpecifiedTest extends AbstractCmdTest {

	@Parameterized.Parameters
	public static Collection<Object[]> testCases() {
		return Arrays.asList(new Object[][] {
				{ FLAG_NO_SSL },
				{ VALID_USERPASS_OPTIONS },
				{ FLAG_VERBOSE },
				{ "hostname" },
				{ "hostname:1234" },
				{ merge(FLAG_VERBOSE, "hostname:1234") },
				{ merge(OPT_CONFIG_SHORT, "Configuration") },
				{ merge(OPT_STARTSESSION_NAME, "Sessionname") },
				{ merge(OPT_SYSTEM_PROFILE, "profilename", OPT_RECORDINGOPTION, "<recoption>") }
		});
	}

	public Cmd_NoCommandSpecifiedTest(String arguments) {
		super(arguments);
	}

	@Test
	public void test() {
		String out = run();

		assertTrue("Missing message for arguments: '" + arguments + "'. Output:\n" + out,
				StringUtils.contains(out, MSG_NO_COMMAND_SPECIFIED));
		assertTrue("Missing usage for arguments: '" + arguments + "'. Output:\n" + out,
				StringUtils.contains(out, USAGE_PREFIX));
	}
}
