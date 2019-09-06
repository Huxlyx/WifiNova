package de.mario.nova.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import de.mario.nova.command.control.NovaCommand;
import de.mario.nova.command.dataunit.DeviceIdDataUnit;
import de.mario.nova.command.dataunit.DeviceTypeDataUnit;
import de.mario.nova.command.util.NovaCommandUtil;
import de.mario.nova.command.util.NovaCommandUtil.CommandIdentifier;
import de.mario.nova.command.util.NovaCommandUtil.DataUnitIdentifier;
import de.mario.nova.command.util.NovaCommandUtil.DeviceTypeIdentifier;

public class CommandTest {

	@Test
	public void testHandshakeCommand() throws IOException {
		final NovaCommand input = new NovaCommand(CommandIdentifier.HANDSHAKE);
		input.addDataUnit(new DeviceTypeDataUnit(DeviceTypeIdentifier.LIGHT_CONTROLLER));
		input.addDataUnit(new DeviceIdDataUnit(Short.MIN_VALUE));

		final ByteArrayOutputStream bos = new ByteArrayOutputStream(12);

		input.writeCommand(bos);
		final byte[] cmdBytes = bos.toByteArray();

		assertEquals(12, cmdBytes.length);
		assertEquals(CommandIdentifier.HANDSHAKE, input.getCommandIdentifier());
		assertEquals(2, input.getDataUnits().size());
		
		assertEquals(DataUnitIdentifier.TYPE, input.getDataUnits().get(0).getIdentifier());
		assertEquals(DataUnitIdentifier.ID, input.getDataUnits().get(1).getIdentifier());
	}

	public void testDecomposedHandshakeCommand() throws IOException {		
		final NovaCommand input = new NovaCommand(CommandIdentifier.HANDSHAKE);
		input.addDataUnit(new DeviceTypeDataUnit(DeviceTypeIdentifier.LIGHT_CONTROLLER));
		input.addDataUnit(new DeviceIdDataUnit(Short.MIN_VALUE));

		final ByteArrayOutputStream bos = new ByteArrayOutputStream(12);

		input.writeCommand(bos);
		final byte[] cmdBytes = bos.toByteArray();

		final NovaCommand output = NovaCommandUtil.composeFromBytes(CommandIdentifier.HANDSHAKE, Arrays.copyOfRange(cmdBytes, 3, 12));

		assertEquals(CommandIdentifier.HANDSHAKE, output.getCommandIdentifier());
		assertEquals(2, output.getDataUnits().size());
		
		assertEquals(DataUnitIdentifier.TYPE, output.getDataUnits().get(0).getIdentifier());
		assertEquals(DataUnitIdentifier.ID, output.getDataUnits().get(1).getIdentifier());
	}
	

}
