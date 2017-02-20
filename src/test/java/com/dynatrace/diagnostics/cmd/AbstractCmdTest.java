package com.dynatrace.diagnostics.cmd;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.dynatrace.diagnostics.cmd.Constants.*;

/**
 * @author Dariusz.Glugla
 */
public abstract class AbstractCmdTest {

	static final String VALID_USERPASS_OPTIONS = merge(FLAG_NO_SSL, OPT_USER, "admin", OPT_PASS, "admin");
	static final String USAGE_PREFIX = "Usage: " + DT_CMD;

	private final ByteArrayOutputStream outContent;
	private final Cmd cmd;

	String arguments;

	@Before
	public void setUp() {
		System.setOut(new PrintStream(outContent));
	}

	@After
	public void tearDown() {
		outContent.reset();
	}

	AbstractCmdTest(String arguments) {
		this.arguments = arguments;
		this.outContent = new ByteArrayOutputStream();
		this.cmd = new Cmd();
	}

	final String run() {
		cmd.run(arguments.split(" "));
		return outContent.toString();
	}

	static String merge(String... strings) {
		return StringUtils.join(strings, ' ');
	}
}
