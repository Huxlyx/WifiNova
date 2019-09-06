package de.mario.nova.command.light;

public class RGB {
	
	private final byte R;
	private final byte G;
	private final byte B;
	
	public RGB(final byte R, final byte G, final byte B) {
		this.R = R;
		this.G = G;
		this.B = B;
	}
	
	public static RGB fromBytes(final byte[] bytes) {
		if (bytes.length != 3) {
			throw new IllegalArgumentException("Expected byte array of length 3 but got " + bytes.length);
		}
		return new RGB(bytes[0], bytes[1], bytes[2]);
	}
	
	public byte[] getBytes() {
		return new byte[] {R, G, B};
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(40);
		sb.append("<Red: ").append((short) R)
		.append(", Green: ").append((short) G)
		.append(", Blue: ").append((short) B).append('>');
		return sb.toString();
	}
}
