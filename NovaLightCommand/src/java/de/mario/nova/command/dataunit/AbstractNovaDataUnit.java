package de.mario.nova.command.dataunit;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.mario.nova.Logging;
import de.mario.nova.command.util.ByteUtil;
import de.mario.nova.command.util.NovaCommandUtil.DataUnitIdentifier;

/**
 * Abstract base class for data units.
 * A data unit is contained of a {@link DataUnitIdentifier}, the length of the payload (possibly 0)
 * and the optional payload itself.
 * <pre>
 *    HEADER
 * +----+--------+---------+
 * | ID | LENGTH | PAYLOAD |
 * +----+--------+---------+
 * </pre>
 */
public abstract class AbstractNovaDataUnit {
	
	public static final int HEADER_SIZE = 3;
	
	private static final Logger LOG = LogManager.getLogger(Logging.COMMAND);
	
	final DataUnitIdentifier identifier;
	final short length;
	final byte[] lengthBytes;
	
	public AbstractNovaDataUnit(final DataUnitIdentifier identifier, final short length) {
		this.identifier = identifier;
		this.length = length;
		this.lengthBytes = ByteUtil.shortToBytes(length);
	}
	
	/**
	 * @return the {@link DataUnitIdentifier} associated with this data unit.
	 */ 
	public DataUnitIdentifier getIdentifier() {
		return identifier;
	}
	
	/**
	 * @return the length of the data unit's payload (possibly 0).
	 */
	public short getLength() {
		return length;
	}
	
	/**
	 * Writes this data unit to the provided output stream.
	 * 
	 * @param os the {@link OutputStream}; not {@code null}
	 * @throws IOException 
	 */
	public void writeDataUnit(final OutputStream os) throws IOException {
		LOG.trace(() -> "Writing data unit");

		os.write(identifier.getIdentifier());
		LOG.trace(() -> "    identifier " + identifier.getIdentifier());

		os.write(lengthBytes);
		LOG.trace(() -> "    length bytes " + ByteUtil.toDebugString(lengthBytes));

		final byte[] payload = getPayload();
		os.write(payload);
		LOG.trace(() -> "    payload " + ByteUtil.toDebugString(payload));
	}
	
	/**
	 * @return the payload of the data unit or a byte array with length 0; not {@code null}
	 */
	protected abstract byte[] getPayload();
	
	/**
	 * @return a string representation of the data unit for debug purposes; not {@code null}
	 */
	protected abstract String toStringDebug();
	
	@Override
	public String toString() {
		return toStringDebug();
	}

}
