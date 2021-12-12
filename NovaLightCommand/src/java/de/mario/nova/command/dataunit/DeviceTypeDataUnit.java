package de.mario.nova.command.dataunit;

import de.mario.nova.command.util.NovaCommandUtil;
import de.mario.nova.command.util.NovaCommandUtil.DataUnitIdentifier;
import de.mario.nova.command.util.NovaCommandUtil.DeviceTypeIdentifier;

public class DeviceTypeDataUnit extends AbstractNovaDataUnit {
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
		if (length != 1) {
			throw new DataUnitException("Expected length 1 but got " + bytes.length);
		}
		final NovaCommandUtil.DeviceTypeIdentifier type = NovaCommandUtil.DeviceTypeIdentifier.fromByte(bytes[offset + 3]);
		return new DeviceTypeDataUnit(type);
	}

}
