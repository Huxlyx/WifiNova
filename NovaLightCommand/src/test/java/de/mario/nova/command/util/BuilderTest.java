package de.mario.nova.command.util;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.mario.nova.command.control.NovaCommand;
import de.mario.nova.command.dataunit.AbstractNovaDataUnit;
import de.mario.nova.command.dataunit.BroadcastDataUnit;
import de.mario.nova.command.dataunit.RGBDataUnit;
import de.mario.nova.command.util.builder.Color;
import de.mario.nova.command.util.builder.NovaLightCommandBuilder;

public class BuilderTest {
	
	@Test
	public void testSimpleRedCommand() throws IOException {
		final byte[] cmdBytes = getSingleRGBBytes(Color.RED);
		
		assertEquals((byte) 0xFF, cmdBytes[3]);
		assertEquals((byte) 0x00, cmdBytes[4]);
		assertEquals((byte) 0x00, cmdBytes[5]);
	}
	
	@Test
	public void testSimpleExplicitCommand() throws IOException {
		final byte[] cmdBytes = getSingleExplicitRGBBytes(1, 23, 45);
		
		assertEquals((byte) 1, cmdBytes[3]);
		assertEquals((byte) 23, cmdBytes[4]);
		assertEquals((byte) 45, cmdBytes[5]);
	}
	
	@Test
	public void testSimpleGreenCommand() throws IOException {
		final byte[] cmdBytes = getSingleRGBBytes(Color.GREEN);
		
		assertEquals((byte) 0x00, cmdBytes[3]);
		assertEquals((byte) 0xFF, cmdBytes[4]);
		assertEquals((byte) 0x00, cmdBytes[5]);
	}
	
	@Test
	public void testSimpleBlueCommand() throws IOException {
		final byte[] cmdBytes = getSingleRGBBytes(Color.BLUE);
		
		assertEquals((byte) 0x00, cmdBytes[3]);
		assertEquals((byte) 0x00, cmdBytes[4]);
		assertEquals((byte) 0xFF, cmdBytes[5]);
	}
	
	@Test
	public void testSimpleBlackCommand() throws IOException {
		final byte[] cmdBytes = getSingleRGBBytes(Color.BLACK);
		
		assertEquals((byte) 0x00, cmdBytes[3]);
		assertEquals((byte) 0x00, cmdBytes[4]);
		assertEquals((byte) 0x00, cmdBytes[5]);
	}
	
	@Test
	public void testSimpleWhiteCommand() throws IOException {
		final byte[] cmdBytes = getSingleRGBBytes(Color.WHITE);
		
		assertEquals((byte) 0xFF, cmdBytes[3]);
		assertEquals((byte) 0xFF, cmdBytes[4]);
		assertEquals((byte) 0xFF, cmdBytes[5]);
	}
	

	
	private byte[] getSingleRGBBytes(final Color color) throws IOException {
		final NovaCommand testCmd = NovaLightCommandBuilder.newCommand()
				.asBroadcast()
				.addRGB(color)
				.finish();
		
		final List<AbstractNovaDataUnit> dataUnits = testCmd.getDataUnits();
		
		assertEquals(2, dataUnits.size());
		
		assertTrue(dataUnits.get(0) instanceof BroadcastDataUnit);
		assertTrue(dataUnits.get(1) instanceof RGBDataUnit);
		
		final RGBDataUnit testUnit = (RGBDataUnit) dataUnits.get(1);
		
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(3);
		testUnit.writeDataUnit(bos);
		final byte[] cmdBytes = bos.toByteArray();
		assertEquals(6, cmdBytes.length);
		return cmdBytes;
	}
	

	
	private byte[] getSingleExplicitRGBBytes(final int r, final int g, final int b) throws IOException {
		final NovaCommand testCmd = NovaLightCommandBuilder.newCommand()
				.asBroadcast()
				.addRGB(r, g, b)
				.finish();
		
		final List<AbstractNovaDataUnit> dataUnits = testCmd.getDataUnits();
		
		assertEquals(2, dataUnits.size());
		
		assertTrue(dataUnits.get(0) instanceof BroadcastDataUnit);
		assertTrue(dataUnits.get(1) instanceof RGBDataUnit);
		
		final RGBDataUnit testUnit = (RGBDataUnit) dataUnits.get(1);
		
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(3);
		testUnit.writeDataUnit(bos);
		final byte[] cmdBytes = bos.toByteArray();
		assertEquals(6, cmdBytes.length);
		return cmdBytes;
	}
}
