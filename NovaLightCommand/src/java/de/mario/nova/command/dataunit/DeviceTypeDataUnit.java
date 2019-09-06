package de.mario.nova.command.dataunit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.mario.nova.Logging;
import de.mario.nova.command.util.NovaCommandUtil;
import de.mario.nova.command.util.NovaCommandUtil.DataUnitIdentifier;
import de.mario.nova.command.util.NovaCommandUtil.DeviceTypeIdentifier;

public class DeviceTypeDataUnit extends AbstractNovaDataUnit {
	private static final Logger LOG = LogManager.getLogger(Logging.COMMAND);

	final DeviceTypeIdentifier type;
	
	public DeviceTypeDataUnit(final DeviceTypeIdentifier type) {
		super(DataUnitIdentifier.TYPE, (short) 1);
		this.type = type;
		LOG.trace(() -> "New DeviceTypeDataUnit. Target: " + type);
	}
	
	public DeviceTypeIdentifier getDeviceType() {
		return type;
	}

	@Override
	protected byte[] getPayload() {
		return new byte[] {type.getIdentifier()};
	}

	@Override
	protected String toStringDebug() {
		final StringBuilder sb = new StringBuilder(32);
		sb.append(this.getClass().getSimpleName()).append(" Type: ").append(type);
		return sb.toString();
	}

	public static DeviceTypeDataUnit fromBytes(final byte[] bytes, final int offset, final int length) {
		if (length != 4) {
			throw new IllegalArgumentException("Excepted byte array with length 5 but got " + bytes.length);
		}
		final NovaCommandUtil.DeviceTypeIdentifier type = NovaCommandUtil.DeviceTypeIdentifier.fromByte(bytes[offset + 3]);
		return new DeviceTypeDataUnit(type);
	}

}
