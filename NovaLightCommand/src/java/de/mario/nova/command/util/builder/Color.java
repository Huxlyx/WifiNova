package de.mario.nova.command.util.builder;

public enum Color {
	
	RED((byte) 0xFF, (byte) 0x00, (byte) 0x00),
	
	GREEN((byte) 0x00, (byte) 0xFF, (byte) 0x00),
	
	BLUE((byte) 0x00, (byte) 0x00, (byte) 0xFF),
	
	WHITE((byte) 0xFF, (byte) 0xFF, (byte) 0xFF),
	
	BLACK((byte) 0x00, (byte) 0x00, (byte) 0x00),
	
	SOME_KIND_OF_ORANGE((byte) 226, (byte) 121, (byte) 35),
	
	SOME_KIND_OF_GREENISH((byte) 74, (byte)  150, (byte) 12);
	
	
	public final byte r;
	public final byte g;
	public final byte b;
	
	Color(final byte r, final byte g, final byte b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
}
