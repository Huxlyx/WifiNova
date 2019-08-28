package de.mario.nova.command;

public class NovaCommandUtil {

	public static final byte LIGHT_CONTROLLER = 0x0A;
	public static final byte COMMAND_CLIENT = 0x0B;

	public static final byte HANDHSKAE = (byte) 0xFF;

	public enum LightCommand {
		STATIC_LIGHT((byte) 0x01),
		LIGHT_TRANSITION((byte) 0x02);
		
		private final byte commandByte;
		
		LightCommand(final byte commandByte) {
			this.commandByte = commandByte;
		}
		
		public byte getCommandByte() {
			return commandByte;
		}
	}
}
