package de.mario.nova.command.util;

public class ByteUtil {
	
	private ByteUtil() throws IllegalAccessException {
		throw new IllegalAccessException("Do not instantiate util class!");
	}

	public static byte[] shortToBytes(final short val) {
		return new byte[] { (byte) (val >> 8), (byte) val };
	}

	public static short bytesToShort(final byte b1, final byte b2) {
		return (short) (b1 << 8 | b2 & 0xFF);
	}
	
	public static String toDebugString(final byte[] bytes) {
		if (bytes == null) {
			return "";
		}
		
		final StringBuilder sb = new StringBuilder(bytes.length * 2);
		
		for (final byte b : bytes) {
			for (int i = 0; i < 2; ++i) {
				final int halfByte = i == 0 ? (b >> 4) & 0x0F : b & 0x0F;
				
				if (halfByte >= 10) {
					switch (halfByte) {
					case 10:
						sb.append('A');
						break;
					case 11:
						sb.append('B');
						break;
					case 12:
						sb.append('C');
						break;
					case 13:
						sb.append('D');
						break;
					case 14:
						sb.append('E');
						break;
					case 15:
						sb.append('F');
						break;
					default:
						throw new IllegalArgumentException("Unexpected value " + halfByte);
					}
				} else {
					sb.append(halfByte);
				}
			}
		}
		
		return sb.toString();
	}

}
