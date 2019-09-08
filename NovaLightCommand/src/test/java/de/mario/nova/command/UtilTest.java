package de.mario.nova.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import de.mario.nova.command.util.ByteUtil;
import de.mario.nova.command.util.NovaCommandUtil.CommandIdentifier;
import de.mario.nova.command.util.NovaCommandUtil.DataUnitIdentifier;
import de.mario.nova.command.util.NovaCommandUtil.DeviceTypeIdentifier;

public class UtilTest {
	
	/* sanity checks for enums */
	@Test
	public void testDeviceTypeIdentifiersForUniqueValues() {
		final DeviceTypeIdentifier[] identifiers = DeviceTypeIdentifier.values();
		final Set<Byte> byteSet = new HashSet<>();
		
		for (final DeviceTypeIdentifier identifier : identifiers) {
			assertTrue(byteSet.add(identifier.getIdentifier()));
		}
	}
	
	@Test
	public void testDataUnitIdentifiersForUniqueValues() {
		final DataUnitIdentifier[] identifiers = DataUnitIdentifier.values();
		final Set<Byte> byteSet = new HashSet<>();
		
		for (final DataUnitIdentifier identifier : identifiers) {
			assertTrue(byteSet.add(identifier.getIdentifier()));
		}
	}
	
	@Test
	public void testCommandIdentifiersForUniqueValues() {
		final CommandIdentifier[] identifiers = CommandIdentifier.values();
		final Set<Byte> byteSet = new HashSet<>();
		
		for (final CommandIdentifier identifier : identifiers) {
			assertTrue(byteSet.add(identifier.getIdentifier()));
		}
	}
	
	@Test
	public void testDeviceTypeIdentifiersFromByte() {
		final DeviceTypeIdentifier[] identifiers = DeviceTypeIdentifier.values();

		for (final DeviceTypeIdentifier identifier : identifiers) {
			assertEquals(identifier, DeviceTypeIdentifier.fromByte(identifier.getIdentifier()));
		}
	}
	
	@Test
	public void testDataUnitIdentifiersFromByte() {
		final DataUnitIdentifier[] identifiers = DataUnitIdentifier.values();

		for (final DataUnitIdentifier identifier : identifiers) {
			assertEquals(identifier, DataUnitIdentifier.fromByte(identifier.getIdentifier()));
		}
	}
	
	@Test
	public void testCommandIdentifiersFromByte() {
		final CommandIdentifier[] identifiers = CommandIdentifier.values();

		for (final CommandIdentifier identifier : identifiers) {
			assertEquals(identifier, CommandIdentifier.fromByte((byte) (identifier.getIdentifier() & 0xFF)));
		}
	}
	
	@Test
	public void testByteUtiltoDebugStringAllBytes() {
		final byte[] bytes = new byte[] { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF };
		final String result = ByteUtil.toDebugString(bytes);
		assertEquals("0123456789ABCDEF", result);
	}
	
	@Test
	public void testByteUtiltoDebugStringNull() {
		final byte[] bytes = null;
		final String result = ByteUtil.toDebugString(bytes);
		assertEquals("", result);
	}
	
	@Test
	public void testByteUtiltoDebugStringEmpty() {
		final byte[] bytes = new byte[0];
		final String result = ByteUtil.toDebugString(bytes);
		assertEquals("", result);
	}

}
