package com.dynatrace.diagnostics.cmd.startup;

import java.io.File;


/**
 * @author Dariusz.Glugla
 * @author cwat-hploch
 */
public class StartupCommandBuilderFactory {

	public static AbstractStartupCommandBuilder startupCommandBuilder(OS os, File serverLaunchDir) {
		switch (os) {
			case Windows:
				return new WindowsStartupCommandBuilder(serverLaunchDir);
			case Linux:
				return new LinuxStartupCommandBuilder(serverLaunchDir);
		}
		return null;
	}

	public enum OS {
		Windows,
		Linux
	}

}
