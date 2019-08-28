package de.mario.nova.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.mario.nova.command.NovaCommandUtil.LightCommand;

public class LightTransitionCommand extends AbstractNovaCommand {
	
	final byte[] cmdBytes;

	public LightTransitionCommand(final short origin, final short target, final byte[] rgb1, final byte[] rgb2, final short durationMillis) {
		super(origin, target, 8, LightCommand.LIGHT_TRANSITION);
		cmdBytes = new byte[8];
		cmdBytes[0] = rgb1[0];
		cmdBytes[1] = rgb1[1];
		cmdBytes[2] = rgb1[2];
		cmdBytes[3] = (byte) (durationMillis >> 8);
		cmdBytes[4] = (byte) durationMillis;
		cmdBytes[5] = rgb2[0];
		cmdBytes[6] = rgb2[1];
		cmdBytes[7] = rgb2[2];
	}

	@Override
	public byte[] getCmdBytes() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(getCmdHeader());
			outputStream.write(cmdBytes);
		} catch (IOException e) {
			//TODO: Handle!
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = getToStringHeader();
		sb.append(" rgb1: [")
		.append(cmdBytes[5]).append(',').append(cmdBytes[6]).append(',').append(cmdBytes[7])
		.append(']')
		.append(" rgb2: [")
		.append(cmdBytes[10]).append(',').append(cmdBytes[11]).append(',').append(cmdBytes[12])
		.append(']')
		.append(" duration: " ).append((short) ((cmdBytes[8] >> 8 & 0xFF) | cmdBytes[9]));
		return sb.toString();
	}

}
