package de.mario.nova.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import de.mario.nova.command.control.NovaCommand;
import de.mario.nova.command.dataunit.DeviceIdDataUnit;
import de.mario.nova.command.dataunit.DeviceTypeDataUnit;
import de.mario.nova.command.util.NovaCommandUtil.CommandIdentifier;
import de.mario.nova.command.util.NovaCommandUtil.DeviceTypeIdentifier;

public class CommandTest {
	
	@Test
	public void testHandshakeCommand() throws IOException {
		final NovaCommand cmd = new NovaCommand(CommandIdentifier.HANDSHAKE);
		cmd.addDataUnit(new DeviceTypeDataUnit(DeviceTypeIdentifier.LIGHT_CONTROLLER));
		cmd.addDataUnit(new DeviceIdDataUnit(Short.MIN_VALUE));
		
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(12);
		
		cmd.writeCommand(bos);
		final byte[] cmdBytes = bos.toByteArray();
		
		assertEquals(12, cmdBytes.length);
	}

}
