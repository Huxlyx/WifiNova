package de.mario.nova.command.dataunit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.mario.nova.Logging;
import de.mario.nova.command.util.NovaCommandUtil.DataUnitIdentifier;

public class BroadcastDataUnit extends AbstractNovaDataUnit {
	private static final Logger LOG = LogManager.getLogger(Logging.COMMAND);

	public BroadcastDataUnit() {
		super(DataUnitIdentifier.BROADCAST, (short) 0);
		LOG.trace(() -> "New BroadcastDataUnit");
	}

	@Override
	protected byte[] getPayload() {
		return new byte[0];
	}

	@Override
	protected String toStringDebug() {
		return "Broadcast";
	}

	public static BroadcastDataUnit fromBytes(byte[] bytes, final int offset, final int length) {
		if (length != 0) {
			throw new DataUnitException("Excepted length 0 but got " + bytes.length + " (offset " + offset + ")");
		}
		return new BroadcastDataUnit();
	}

}
