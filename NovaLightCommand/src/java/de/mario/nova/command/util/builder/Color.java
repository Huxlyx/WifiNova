package de.mario.nova.command.util.builder;

public enum Color {
	
	RED((byte) 0xFF, (byte) 0x00, (byte) 0x00),
	
	GREEN((byte) 0x00, (byte) 0xFF, (byte) 0x00),
	
	BLUE((byte) 0x00, (byte) 0x00, (byte) 0xFF),
	
	WHITE((byte) 0xFF, (byte) 0xFF, (byte) 0xFF),
	
	BLACK((byte) 0x00, (byte) 0x00, (byte) 0x00);
	
	
	public final byte r;
	public final byte g;
	public final byte b;
	
	Color(final byte r, final byte g, final byte b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
}
