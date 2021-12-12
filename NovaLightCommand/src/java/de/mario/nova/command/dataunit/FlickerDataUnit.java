package de.mario.nova.command.dataunit;

import de.mario.nova.command.light.RGB;
import de.mario.nova.command.util.NovaCommandUtil.DataUnitIdentifier;

public class FlickerDataUnit extends AbstractNovaDataUnit {
	
	final RGB rgb;

	public FlickerDataUnit(final RGB rgb) {
		super(DataUnitIdentifier.FLICKER, (short) 3);
		this.rgb = rgb;
		LOG.trace(() -> "New FlickerEffectDataUnit. " + rgb);
	}

	@Override
	protected byte[] getPayload() {
		return rgb.getBytes();
	}

	@Override
	protected String toStringDebug() {
		final StringBuilder sb = new StringBuilder(32);
		sb.append(this.getClass().getSimpleName()).append(" rgb: ").append(rgb);
		return sb.toString();
	}

	public static FlickerDataUnit fromBytes(final byte[] bytes, final int offset, final int length) {
		if (length != 3) {
			throw new DataUnitException("Expected length 3 but got " + bytes.length);
		}
		final RGB rbg = new RGB(bytes[offset + 3], bytes[offset + 4], bytes[offset + 5]);
		return new FlickerDataUnit(rbg);
	}

}
