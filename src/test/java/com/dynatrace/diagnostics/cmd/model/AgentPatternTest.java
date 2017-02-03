package com.dynatrace.diagnostics.cmd.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Dariusz.Glugla
 */
public class AgentPatternTest {

	private final AgentPattern.Converter converter = new AgentPattern.Converter();

	@Test
	public void testConvert() {
//		assertEquals(new AgentPattern("agent", "host", 1234), converter.convert("agent@host:1234"));
		assertEquals(new AgentPattern("agent", "host", null), converter.convert("agent@host"));
//		assertEquals(new AgentPattern("agent", null, null), converter.convert("agent"));
	}

	@Test(expected = InternalError.class)
	public void testConvert_NoAgent() {
		converter.convert("@host:1234");
	}

	@Test(expected = InternalError.class)
	public void testConvert_HostAndPortOnly() {
		converter.convert("host:1234");
	}

	@Test(expected = InternalError.class)
	public void testConvert_PortOnly() {
		converter.convert(":1234");
	}

	@Test(expected = InternalError.class)
	public void testConvert_HostOnly() {
		converter.convert("@1234");
	}

	@Test(expected = InternalError.class)
	public void testConvert_DelimitersOnly() {
		converter.convert("@:");
	}

	@Test(expected = InternalError.class)
	public void testConvert_HostWithoutPort() {
		converter.convert("host:");
	}

	@Test(expected = InternalError.class)
	public void testConvert_Empty() {
		converter.convert("");
	}

	@Test(expected = InternalError.class)
	public void testConvert_Null() {
		converter.convert(null);
	}

	@Test(expected = InternalError.class)
	public void testConvert_InvalidPattern() {
		converter.convert("agent:host@id");
	}

	@Test(expected = InternalError.class)
	public void testConvert_NonNumericId() {
		converter.convert("agent@host:id");
	}

	@Test(expected = InternalError.class)
	public void testConvert_NoHost() {
		converter.convert("agent:1234");
	}

}
