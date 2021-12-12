package de.mario.nova.command.control;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.mario.nova.Logging;
import de.mario.nova.command.dataunit.AbstractNovaDataUnit;
import de.mario.nova.command.util.ByteUtil;
import de.mario.nova.command.util.NovaCommandUtil.CommandIdentifier;

/**
 * Command that contains one ore more {@link AbstractNovaDataUnit} implementations.
 */
public class NovaCommand {
	
	private static final Logger LOG = LogManager.getLogger(Logging.COMMAND);
	
	private final List<AbstractNovaDataUnit> dataUnits = new ArrayList<>();
	
	private final CommandIdentifier command;
	private int length;
	
	public static NovaCommand disconnect() {
		return new NovaCommand(CommandIdentifier.DISCONNECT);
	}
	
	/**
	 * Create a new NovaCommand with the provided {@link CommandIdentifier}.
	 * 
	 * @param command the {@code CommandIdentifier}; not {@code null}
	 */
	public NovaCommand(final CommandIdentifier command) {
		this.command = command;
	}
	
	/**
	 * @return the {@link CommandIdentifier} associated with this command
	 */
	public CommandIdentifier getCommandIdentifier() {
		return command;
	}
	
	/**
	 * Adds a {@link AbstractNovaDataUnit} implementation to this command.
	 * 
	 * @param dataUnit the {@code AbstractNovaDataUnit}; not {@code null}
	 */
	public void addDataUnit(final AbstractNovaDataUnit dataUnit) {
		dataUnits.add(dataUnit);
		length += dataUnit.getLength() + AbstractNovaDataUnit.HEADER_SIZE;
		LOG.trace(() -> "Added data unit " + dataUnit + ". Total Length: " + length);
	}
	
	/**
	 * @return returns an unmodifiable list of {@link AbstractNovaDataUnit} that
	 * were added to this command.
	 */
	public List<AbstractNovaDataUnit> getDataUnits() {
		return Collections.unmodifiableList(dataUnits);
	}
	
	/**
	 * Writes this command to the provided {@link OutputStream}.
	 * The {@code DataUnits} are preceded by the single byte {@link CommandIdentifier} that's associated with this
	 * command followed by two bytes with the combined length of all {@code DataUnits} contained in this command. 
	 * 
	 * @param os the {@code OutputStream}; not {@code null}
	 * @throws IOException in case of an error writing the command content to the stream
	 */
	public void writeCommand(final OutputStream os) throws IOException {
		/* sanity check that length does not extend beyond legal value */
		if (length > Short.MAX_VALUE) {
			LOG.error(() -> "Length " + length + " exceeds allowed value " + Short.MAX_VALUE);
			throw new IllegalStateException("Length exceeded");
		}
		os.write(new byte[] {command.getIdentifier()});
		os.write(ByteUtil.shortToBytes((short) length));
		for (final AbstractNovaDataUnit dataUnit : dataUnits) {
			dataUnit.writeDataUnit(os);
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
