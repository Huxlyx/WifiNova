package de.mario.nova.command.dataunit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.mario.nova.Logging;
import de.mario.nova.command.util.ByteUtil;
import de.mario.nova.command.util.NovaCommandUtil.DataUnitIdentifier;

public class DurationDataUnit extends AbstractNovaDataUnit {
	private static final Logger LOG = LogManager.getLogger(Logging.COMMAND);

	final short duration;
	final byte[] durationBytes;
	
	public DurationDataUnit(final short duration) {
		super(DataUnitIdentifier.DURATION, (short) 2);
		this.duration = duration;
		this.durationBytes = ByteUtil.shortToBytes(duration);
		LOG.trace(() -> "New DurationDataUnit. Duration: " + duration);
	}
	
	public short getDuration() {
		return duration;
	}

	@Override
	protected byte[] getPayload() {
		return durationBytes;
	}

	@Override
	protected String toStringDebug() {
		final StringBuilder sb = new StringBuilder(32);
		sb.append(this.getClass().getSimpleName()).append(" Duration: ").append(duration);
		return sb.toString();
	}

	public static DurationDataUnit fromBytes(final byte[] bytes, final int offset, final int length) {
		if (length != 2) {
			throw new DataUnitException("Excepted length 2 but got " + bytes.length);
		}
		final short duration = ByteUtil.bytesToShort(bytes[offset + 3], bytes[offset + 4]);
		return new DurationDataUnit(duration);
	}

}
