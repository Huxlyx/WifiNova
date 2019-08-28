package de.mario.nova.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.mario.nova.command.NovaCommandUtil.LightCommand;

public class StaticLightCommand extends AbstractNovaCommand {
	
	final byte[] cmdBytes;

	public StaticLightCommand(final short origin, final short target, final byte[] rgb) {
		super(origin, target, 3, LightCommand.STATIC_LIGHT);
		cmdBytes = new byte[3];
		cmdBytes[0] = rgb[0];
		cmdBytes[1] = rgb[1];
		cmdBytes[2] = rgb[2];
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
		sb.append(" rg1: [")
		.append(cmdBytes[5]).append(',').append(cmdBytes[6]).append(',').append(cmdBytes[7])
		.append(']');
		return sb.toString();
	}

}
