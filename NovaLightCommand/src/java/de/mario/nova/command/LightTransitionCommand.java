package de.mario.nova.command;

import de.mario.nova.command.NovaCommandUtil.LightCommand;

public class LightTransitionCommand extends AbstractNovaCommand {
	
	final byte[] payload;

	public LightTransitionCommand(final short origin, final short target, final byte[] rgb1, final byte[] rgb2, final short durationMillis) {
		super(origin, target, 8, LightCommand.LIGHT_TRANSITION);
		payload = new byte[8];
		payload[0] = rgb1[0];
		payload[1] = rgb1[1];
		payload[2] = rgb1[2];
		payload[3] = (byte) (durationMillis >> 8);
		payload[4] = (byte) durationMillis;
		payload[5] = rgb2[0];
		payload[6] = rgb2[1];
		payload[7] = rgb2[2];
	}

	@Override
	protected byte[] getPayload() {
		return payload;
	}

	@Override
	public String toString() {
		final StringBuilder sb = getToStringHeader();
		sb.append(" rgb1: [")
		.append(payload[0]).append(',').append(payload[1]).append(',').append(payload[2])
		.append(']')
		.append(" rgb2: [")
		.append(payload[5]).append(',').append(payload[6]).append(',').append(payload[7])
		.append(']')
		.append(" duration: " ).append((short) ((payload[3] >> 8 & 0xFF) | payload[4]));
		return sb.toString();
	}

}
