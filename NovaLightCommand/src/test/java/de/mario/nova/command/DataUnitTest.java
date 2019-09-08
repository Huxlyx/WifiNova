package de.mario.nova.command;

import java.io.IOException;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.mario.nova.command.control.NovaCommand;
import de.mario.nova.command.dataunit.BroadcastDataUnit;
import de.mario.nova.command.dataunit.CommandCountDataUnit;
import de.mario.nova.command.dataunit.DataUnitException;
import de.mario.nova.command.dataunit.DeviceIdDataUnit;
import de.mario.nova.command.dataunit.DeviceTypeDataUnit;
import de.mario.nova.command.dataunit.RGBDataUnit;
import de.mario.nova.command.light.RGB;
import de.mario.nova.command.util.NovaCommandUtil.CommandIdentifier;
import de.mario.nova.command.util.NovaCommandUtil.DataUnitIdentifier;
import de.mario.nova.command.util.NovaCommandUtil.DeviceTypeIdentifier;

public class DataUnitTest {

	@Test
	public void testDataUnitToStringMethods() throws IOException {
		final NovaCommand cmd = new NovaCommand(CommandIdentifier.LIGHT_COMMAND);
		cmd.addDataUnit(new BroadcastDataUnit());
		cmd.addDataUnit(new CommandCountDataUnit(Short.MIN_VALUE));
		cmd.addDataUnit(new DeviceIdDataUnit(Short.MIN_VALUE));
		cmd.addDataUnit(new DeviceTypeDataUnit(DeviceTypeIdentifier.COMMAND_CLIENT));
		cmd.addDataUnit(new RGBDataUnit(new RGB((byte) 0x01, (byte) 0x23, (byte) 0x45)));
		cmd.toString();
	}
	
	@Test
	public void testBroadcastDataUnit() {
		final BroadcastDataUnit broadcast = new BroadcastDataUnit();
		Assert.assertEquals(DataUnitIdentifier.BROADCAST, broadcast.getIdentifier());
		Assert.assertEquals(0, broadcast.getLength());
	}
	
	@Test
	public void testConstructedBroadcastUnit() {
		final byte[] data = new byte[0];
		final BroadcastDataUnit constructedDataUnit = BroadcastDataUnit.fromBytes(data, 0, 0);
		Assert.assertEquals(DataUnitIdentifier.BROADCAST, constructedDataUnit.getIdentifier());
		Assert.assertEquals(0, constructedDataUnit.getLength());
	}
	
	@Test
	public void testConstructedBroadcastForDataUnitException() {
		final byte[] data = new byte[0];
		Assertions.assertThrows(DataUnitException.class, () -> BroadcastDataUnit.fromBytes(data, 0, 1));
	}
}
