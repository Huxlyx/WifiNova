package de.mario.nova.command.dataunit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.mario.nova.Logging;
import de.mario.nova.command.util.NovaCommandUtil;
import de.mario.nova.command.util.NovaCommandUtil.DataUnitIdentifier;

public class DeviceIdDataUnit extends AbstractNovaDataUnit {
	private static final Logger LOG = LogManager.getLogger(Logging.COMMAND);

	final byte[] idBytes;
	final short target;
	
	public DeviceIdDataUnit(final short target) {
		super(DataUnitIdentifier.ID, (short) 2);
		this.target = target;
		this.idBytes = NovaCommandUtil.shortToBytes(target);
		LOG.trace(() -> "New DeviceIdDataUnit. Target: " + target);
	}
	
	public short getTarget() {
		return target;
	}

	@Override
	protected byte[] getPayload() {
		return idBytes;
	}

	@Override
	protected String toStringDebug() {
		final StringBuilder sb = new StringBuilder(32);
		sb.append(this.getClass().getSimpleName()).append(" Target: ").append(target);
		return sb.toString();
	}

	public static DeviceIdDataUnit fromBytes(final byte[] bytes, final int offset, final int length) {
		if (length != 5) {
			throw new IllegalArgumentException("Excepted byte array with length 5 but got " + bytes.length);
		}
		final short id = NovaCommandUtil.bytesToShort(bytes[offset + 3], bytes[offset + 4]);
		return new DeviceIdDataUnit(id);
	}

}
