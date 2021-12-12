package de.mario.nova.command.dataunit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.mario.nova.command.light.RGB;
import de.mario.nova.command.util.ByteUtil;
import de.mario.nova.command.util.NovaCommandUtil.DataUnitIdentifier;

public class GradientDataUnit extends AbstractNovaDataUnit {
	
	final RGB rgb1;
	final RGB rgb2;
	final short duration;

	public GradientDataUnit(final RGB rgb1, final RGB rgb2, final short duration) {
		super(DataUnitIdentifier.GRADIENT, (short) 8);
		this.rgb1 = rgb1;
		this.rgb2 = rgb2;
		this.duration = duration;
		LOG.trace(() -> "New RGBDataUnit. " + rgb1 + " " + rgb2 + " " + duration);
	}

	@Override
	protected byte[] getPayload() {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
		try {
			bos.write(rgb1.getBytes());
			bos.write(rgb2.getBytes());
			bos.write(ByteUtil.shortToBytes(duration));
		} catch (IOException e) {
			LOG.error("Could not create payload array for GradientDataUnit " + toStringDebug(), e);
		}
		return bos.toByteArray();
	}

	@Override
	protected String toStringDebug() {
		final StringBuilder sb = new StringBuilder(32);
		sb.append(this.getClass().getSimpleName()).append(" rgb1: ").append(rgb1).append(' ');
		sb.append(" rgb2: ").append(rgb2).append(' ');
		sb.append(" duration: ").append(duration);
		return sb.toString();
	}

	public static GradientDataUnit fromBytes(final byte[] bytes, final int offset, final int length) {
		if (length != 8) {
			throw new DataUnitException("Expected length 8 but got " + bytes.length);
		}
		final RGB rgb1 = new RGB(bytes[offset + 3], bytes[offset + 4], bytes[offset + 5]);
		final RGB rgb2 = new RGB(bytes[offset + 6], bytes[offset + 7], bytes[offset + 8]);
		final short duration = ByteUtil.bytesToShort(bytes[offset + 9], bytes[offset + 10]);
		return new GradientDataUnit(rgb1, rgb2, duration);
	}

}
