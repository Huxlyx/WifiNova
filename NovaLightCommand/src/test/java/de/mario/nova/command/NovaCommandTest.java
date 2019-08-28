package de.mario.nova.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NovaCommandTest {
	
	@Test
	public void testLightTransissionCommand() {
		
		final byte[] rgb1 = new byte[] {12, 23, 34};
		final byte[] rgb2 = new byte[] {34, 45, 56};
		final LightTransitionCommand cmd = new LightTransitionCommand((short) 0, (short) 1, rgb1, rgb2, (short) 1234);
		
		assertEquals(7, cmd.cmdHeader.length);
		assertEquals(8, cmd.payload.length);
		
		assertEquals(0, cmd.origin);
		assertEquals(1, cmd.target);
	}

}
