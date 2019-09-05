package de.mario.nova.command;

import de.mario.nova.command.NovaCommandUtil.LightCommand;

public class StaticLightCommand extends AbstractNovaCommand {
	
	final byte[] payload;

	public StaticLightCommand(final short origin, final short target, final byte[] rgb) {
		super(origin, target, 3, LightCommand.STATIC_LIGHT);
		payload = new byte[3];
		payload[0] = rgb[0];
		payload[1] = rgb[1];
		payload[2] = rgb[2];
	}

	@Override
	protected byte[] getPayload() {
		return payload;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = getToStringHeader();
		sb.append(" rg1: [")
		.append(payload[0]).append(',').append(payload[1]).append(',').append(payload[2])
		.append(']');
		return sb.toString();
	}

}
