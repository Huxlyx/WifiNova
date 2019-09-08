package de.mario.nova.command.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.mario.nova.Logging;
import de.mario.nova.command.control.NovaCommand;
import de.mario.nova.command.dataunit.AbstractNovaDataUnit;
import de.mario.nova.command.dataunit.BroadcastDataUnit;
import de.mario.nova.command.dataunit.DeviceIdDataUnit;
import de.mario.nova.command.dataunit.DeviceTypeDataUnit;
import de.mario.nova.command.dataunit.RGBDataUnit;

public class NovaCommandUtil {
	
	private static final Logger LOG = LogManager.getLogger(Logging.COMMAND);

	/* device identifier */
	public enum DeviceTypeIdentifier {

		LIGHT_CONTROLLER((byte) 0x0A),

		COMMAND_CLIENT((byte) 0x0B);


		private final byte identifier;

		DeviceTypeIdentifier(final byte identifier) {
			this.identifier = identifier;
		}

		public byte getIdentifier() {
			return identifier;
		}

		public static DeviceTypeIdentifier fromByte(final byte identifier) {
			final DeviceTypeIdentifier[] values = DeviceTypeIdentifier.values();
			for (final DeviceTypeIdentifier value : values) {
				if (value.getIdentifier() == identifier) {
					return value;
				}
			}
			throw new IllegalArgumentException("Unkown DeviceIdentifier with identifier byte " + identifier);
		}
	}

	/* data unit identifier */
	public enum DataUnitIdentifier {
		RGB((byte) 0x01),

		ID ((byte) 0x02),

		TYPE ((byte) 0x03),

		DURATION ((byte) 0x04),

		COMMAND_COUNT ((byte) 0x05),

		BROADCAST((byte) 0x0A);

		private final byte identifier;

		DataUnitIdentifier(final byte identifier) {
			this.identifier = identifier;
		}

		public byte getIdentifier() {
			return identifier;
		}

		public static DataUnitIdentifier fromByte(final byte identifier) {
			final DataUnitIdentifier[] values = DataUnitIdentifier.values();
			for (final DataUnitIdentifier value : values) {
				if (value.getIdentifier() == identifier) {
					return value;
				}
			}
			throw new IllegalArgumentException("Unkown DataUnitIdentifier with identifier byte " + identifier);
		}
	}

	/* command identifier */
	public enum CommandIdentifier {

		HANDSHAKE ((byte) 0xFF),

		LIGHT_COMMAND((byte) 0x01),
		
		DEVICE_UPDATE((byte) 0x02);

		private final byte identifier;

		CommandIdentifier(final byte identifier) {
			this.identifier = identifier;
		}

		public byte getIdentifier() {
			return identifier;
		}

		public static CommandIdentifier fromByte(final byte identifier) {
			final CommandIdentifier[] values = CommandIdentifier.values();
			for (final CommandIdentifier value : values) {
				if (value.getIdentifier() == identifier) {
					return value;
				}
			}
			throw new IllegalArgumentException("Unkown CommandIdentifier with identifier byte " + identifier);
		}
	}

	public static NovaCommand composeFromBytes(final CommandIdentifier command, final byte[] bytes) {
		final NovaCommand cmd = new NovaCommand(command);

		int index = 0;
		while (index < bytes.length) {
			final byte dataUnitIdentifier = bytes[index];
			final short length = ByteUtil.bytesToShort(bytes[index + 1], bytes[index + 2]);

			final DataUnitIdentifier identifier = DataUnitIdentifier.fromByte(dataUnitIdentifier);
			switch (identifier) {
			case BROADCAST:
				cmd.addDataUnit(BroadcastDataUnit.fromBytes(bytes, index, length));
				break;
			case TYPE:
				cmd.addDataUnit(DeviceTypeDataUnit.fromBytes(bytes, index, length));
				break;
			case ID:
				cmd.addDataUnit(DeviceIdDataUnit.fromBytes(bytes, index, length));
				break;
			case RGB:
				cmd.addDataUnit(RGBDataUnit.fromBytes(bytes, index, length));
				break;
			case DURATION:
			default:
				LOG.error("Unhandled identifier type " + identifier);
				break;
			}
			
			index += length + AbstractNovaDataUnit.HEADER_SIZE;
		}
		return cmd;
	}
}
