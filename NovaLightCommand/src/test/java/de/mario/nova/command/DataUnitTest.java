package de.mario.nova.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.mario.nova.command.control.NovaCommand;
import de.mario.nova.command.dataunit.BroadcastDataUnit;
import de.mario.nova.command.dataunit.CommandCountDataUnit;
import de.mario.nova.command.dataunit.DataUnitException;
import de.mario.nova.command.dataunit.DeviceIdDataUnit;
import de.mario.nova.command.dataunit.DeviceTypeDataUnit;
import de.mario.nova.command.dataunit.DurationDataUnit;
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
		assertEquals(DataUnitIdentifier.BROADCAST, broadcast.getIdentifier());
		assertEquals(0, broadcast.getLength());
	}
	
	@Test
	public void testConstructedBroadcastUnit() {
		final byte[] data = new byte[0];
		final BroadcastDataUnit constructedDataUnit = BroadcastDataUnit.fromBytes(data, 0, 0);
		assertEquals(DataUnitIdentifier.BROADCAST, constructedDataUnit.getIdentifier());
		assertEquals(0, constructedDataUnit.getLength());
	}
	
	@Test
	public void testConstructedBroadcastForDataUnitException() {
		final byte[] data = new byte[0];
		Assertions.assertThrows(DataUnitException.class, () -> BroadcastDataUnit.fromBytes(data, 0, 1));
	}
	
	@Test
	public void testDurationDataUnit() {
		final DurationDataUnit duration = new DurationDataUnit((short) 123);
		assertEquals(DataUnitIdentifier.DURATION, duration.getIdentifier());
		assertEquals(2, duration.getLength());
	}
	
	@Test
	public void testConstructedDurationDataUnit() {
		final byte[] data = new byte[] { 0x00, 0x00, 0x02, 0x12, 0x34 };
		final DurationDataUnit constructedDataUnit = DurationDataUnit.fromBytes(data, 0, 2);
		assertEquals(DataUnitIdentifier.DURATION, constructedDataUnit.getIdentifier());
		assertEquals(2, constructedDataUnit.getLength());
		assertEquals(4660, constructedDataUnit.getDuration());
	}
}
