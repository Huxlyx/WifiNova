package de.mario.nova.command.light;

public class RGB {
	
	private final byte r;
	private final byte g;
	private final byte b;
	
	public RGB(final byte R, final byte G, final byte B) {
		this.r = R;
		this.g = G;
		this.b = B;
	}
	
	public static RGB fromBytes(final byte[] bytes) {
		if (bytes.length != 3) {
			throw new IllegalArgumentException("Expected byte array of length 3 but got " + bytes.length);
		}
		return new RGB(bytes[0], bytes[1], bytes[2]);
	}
	
	public byte[] getBytes() {
		return new byte[] {r, g, b};
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(40);
		sb.append("<Red: ").append(r & 0xFF)
		.append(", Green: ").append(g & 0xFF)
		.append(", Blue: ").append(b & 0xFF).append('>');
		return sb.toString();
	}
}
