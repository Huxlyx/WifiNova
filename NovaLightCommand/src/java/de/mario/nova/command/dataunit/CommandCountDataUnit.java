package de.mario.nova.command.dataunit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.mario.nova.Logging;
import de.mario.nova.command.util.NovaCommandUtil.DataUnitIdentifier;

public class CommandCountDataUnit extends AbstractNovaDataUnit {
	private static final Logger LOG = LogManager.getLogger(Logging.COMMAND);

	final byte countByte;
	final short count;
	
	public CommandCountDataUnit(final short count) {
		super(DataUnitIdentifier.COMMAND_COUNT, (short) 1);
		this.count = count;
		if (count > 255) {
			throw new DataUnitException("Invalid command count " + count);
		}
		this.countByte = (byte) (count & 0xFF);
		LOG.trace(() -> "New CommandCountDataUnit. Count: " + count);
	}
	
	public short getCommandCount() {
		return count;
	}

	@Override
	protected byte[] getPayload() {
		return new byte[] {countByte};
	}

	@Override
	protected String toStringDebug() {
		final StringBuilder sb = new StringBuilder(32);
		sb.append(this.getClass().getSimpleName()).append(" Count: ").append(count);
		return sb.toString();
	}

	public static CommandCountDataUnit fromBytes(final byte[] bytes, final int offset, final int length) {
		if (length != 1) {
			throw new DataUnitException("Excepted length 2 but got " + bytes.length);
		}
		return new CommandCountDataUnit((short) bytes[offset + 3]);
	}

}
