package de.mario.nova.command.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import de.mario.nova.command.util.builder.Color;
import de.mario.nova.command.util.builder.NovaLightCommandBuilder;

public class BuilderTest {
	
	@Test
	public void testSimpleRedCommand() throws IOException {
		final byte[] cmdBytes = getSingleRGBBytes(Color.RED);
		
		assertEquals((byte) 0xFF, cmdBytes[9]);
		assertEquals((byte) 0x00, cmdBytes[10]);
		assertEquals((byte) 0x00, cmdBytes[11]);
	}
	
	@Test
	public void testSimpleExplicitCommand() throws IOException {
		final byte[] cmdBytes = getSingleExplicitRGBBytes(1, 23, 45);
		
		assertEquals((byte) 1, cmdBytes[9]);
		assertEquals((byte) 23, cmdBytes[10]);
		assertEquals((byte) 45, cmdBytes[11]);
	}
	
	@Test
	public void testSimpleGreenCommand() throws IOException {
		final byte[] cmdBytes = getSingleRGBBytes(Color.GREEN);
		
		assertEquals((byte) 0x00, cmdBytes[9]);
		assertEquals((byte) 0xFF, cmdBytes[10]);
		assertEquals((byte) 0x00, cmdBytes[11]);
	}
	
	@Test
	public void testSimpleBlueCommand() throws IOException {
		final byte[] cmdBytes = getSingleRGBBytes(Color.BLUE);
		
		assertEquals((byte) 0x00, cmdBytes[9]);
		assertEquals((byte) 0x00, cmdBytes[10]);
		assertEquals((byte) 0xFF, cmdBytes[11]);
	}
	
	@Test
	public void testSimpleBlackCommand() throws IOException {
		final byte[] cmdBytes = getSingleRGBBytes(Color.BLACK);
		
		assertEquals((byte) 0x00, cmdBytes[9]);
		assertEquals((byte) 0x00, cmdBytes[10]);
		assertEquals((byte) 0x00, cmdBytes[11]);
	}
	
	@Test
	public void testSimpleWhiteCommand() throws IOException {
		final byte[] cmdBytes = getSingleRGBBytes(Color.WHITE);
		
		assertEquals((byte) 0xFF, cmdBytes[9]);
		assertEquals((byte) 0xFF, cmdBytes[10]);
		assertEquals((byte) 0xFF, cmdBytes[11]);
	}
	

	
	private byte[] getSingleRGBBytes(final Color color) throws IOException {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(6);
		NovaLightCommandBuilder.newCommand()
				.asBroadcast()
				.addStaticColor(color)
				.go(bos);
		
		final byte[] cmdBytes = bos.toByteArray();
		assertEquals(12, cmdBytes.length);
		return cmdBytes;
	}
	

	
	private byte[] getSingleExplicitRGBBytes(final int r, final int g, final int b) throws IOException {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(6);
		NovaLightCommandBuilder.newCommand()
				.asBroadcast()
				.addStaticColor(r, g, b)
				.go(bos);
		
		final byte[] cmdBytes = bos.toByteArray();
		assertEquals(12, cmdBytes.length);
		return cmdBytes;
	}
}
