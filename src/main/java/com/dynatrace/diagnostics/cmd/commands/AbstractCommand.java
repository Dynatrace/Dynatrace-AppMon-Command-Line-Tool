package com.dynatrace.diagnostics.cmd.commands;

import com.beust.jcommander.Parameter;
import com.dynatrace.diagnostics.cmd.model.CmdOptions;
import org.apache.commons.lang3.StringUtils;

import static com.dynatrace.diagnostics.cmd.Constants.FLAG_VERBOSE;
import static com.dynatrace.diagnostics.cmd.Constants.TITLE_PREFIX;
import static java.lang.System.out;

/**
 * @author Dariusz.Glugla
 */
public abstract class AbstractCommand {

	@Parameter(names = FLAG_VERBOSE, description = "provide a more detailed output (in case of errors)")
	private Boolean verbose;

	public void run(CmdOptions options) throws Exception {
		printHeader(getHeader());
		runInternal(options);
	}

	private void printHeader(String header) {
		out.println(StringUtils.join(TITLE_PREFIX, header, TITLE_PREFIX, ' '));
	}

	protected abstract void runInternal(CmdOptions options) throws Exception;

	protected abstract String getHeader();

	public Boolean getVerbose() {
		return verbose;
	}

}
