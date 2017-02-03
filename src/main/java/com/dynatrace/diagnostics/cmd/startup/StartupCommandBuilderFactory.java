/***************************************************
 * dynaTrace Diagnostics (c) dynaTrace software GmbH
 *
 * @file: StartupCommandBuilderFactory.java
 * @date: 24.04.2014
 * @author: cwat-hploch
 */
package com.dynatrace.diagnostics.cmd.startup;

import java.io.File;


/**
 * @author Dariusz.Glugla
 * @author cwat-hploch
 */
public class StartupCommandBuilderFactory {

	public static AbstractStartupCommandBuilder startupCommandBuilder(OS os, StringBuilder sb, File serverLaunchDir) {
		switch (os) {
			case Windows:
				return new WindowsStartupCommandBuilder(sb, serverLaunchDir);
			case Linux:
				return new LinuxStartupCommandBuilder(sb, serverLaunchDir);
		}
		return null;
	}

	public enum OS {
		Windows,
		Linux
	}

}
