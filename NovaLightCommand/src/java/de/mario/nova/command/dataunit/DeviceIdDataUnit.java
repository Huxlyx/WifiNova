package de.mario.nova.command.dataunit;

import de.mario.nova.command.util.ByteUtil;
import de.mario.nova.command.util.NovaCommandUtil.DataUnitIdentifier;

public class DeviceIdDataUnit extends AbstractNovaDataUnit {

	final byte[] idBytes;
	final short deviceId;
	
	public DeviceIdDataUnit(final short target) {
		super(DataUnitIdentifier.ID, (short) 2);
		this.deviceId = target;
		this.idBytes = ByteUtil.shortToBytes(target);
		LOG.trace(() -> "New DeviceIdDataUnit. Target: " + target);
	}
	
	public short getDeviceId() {
		return deviceId;
	}

	@Override
	protected byte[] getPayload() {
		return idBytes;
	}

	@Override
	protected String toStringDebug() {
		final StringBuilder sb = new StringBuilder(32);
		sb.append(this.getClass().getSimpleName()).append(" DeviceId: ").append(deviceId);
		return sb.toString();
	}

	public static DeviceIdDataUnit fromBytes(final byte[] bytes, final int offset, final int length) {
		if (length != 2) {
			throw new DataUnitException("Expected length 2 but got " + bytes.length);
		}
		final short id = ByteUtil.bytesToShort(bytes[offset + 3], bytes[offset + 4]);
		return new DeviceIdDataUnit(id);
	}

}
