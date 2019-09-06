package de.mario.nova.command.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omg.CORBA.portable.OutputStream;

import de.mario.nova.Logging;
import de.mario.nova.command.dataunit.AbstractNovaDataUnit;
import de.mario.nova.command.util.NovaCommandUtil;
import de.mario.nova.command.util.NovaCommandUtil.CommandIdentifier;

public class NovaCommand {
	private static final Logger LOG = LogManager.getLogger(Logging.COMMAND);
	
	private final List<AbstractNovaDataUnit> dataUnits = new ArrayList<>();
	
	private final CommandIdentifier command;
	private int length;
	
	public NovaCommand(final CommandIdentifier command) {
		this.command = command;
	}
	
	public CommandIdentifier getCommandIdentifier() {
		return command;
	}
	
	/**
	 * Convenience method to create a command with a single data unit
	 * @param command the command
	 * @param dataUnit the data unit; not {@code null}
	 * @return the new command
	 */
	public static NovaCommand fromDataUnit(final CommandIdentifier command, final AbstractNovaDataUnit dataUnit) {
		final NovaCommand cmd = new NovaCommand(command);
		cmd.addDataUnit(dataUnit);
		return cmd;
	}
	
	public void addDataUnit(final AbstractNovaDataUnit dataUnit) {
		dataUnits.add(dataUnit);
		length += dataUnit.getLength();
		LOG.trace(() -> "Added data unit " + dataUnit + ". Total Length: " + length);
	}
	
	public List<AbstractNovaDataUnit> getDataUnits() {
		return dataUnits;
	}
	
	public void writeCommand(final OutputStream os) {
		/* sanity check that length does not extend beyond legal value */
		if (length > Short.MAX_VALUE) {
			LOG.error(() -> "Length " + length + " exceeds allowed value " + Short.MAX_VALUE);
			throw new IllegalStateException("Length exceeded");
		}
		try {
			os.write(new byte[] {command.getIdentifier()});
			os.write(NovaCommandUtil.shortToBytes((short) length));
			for (final AbstractNovaDataUnit dataUnit : dataUnits) {
				dataUnit.writeDataUnit(os);
			}
		} catch (final IOException e) {
			LOG.error(() -> "Could not write command ", e);
		}
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(256);
		sb.append("NovaCommand ").append(command).append(" Length: ").append(length);
		for (final AbstractNovaDataUnit dataUnit : dataUnits) {
			sb.append('\n').append(dataUnit);
		}
		return sb.toString();
	}
}
