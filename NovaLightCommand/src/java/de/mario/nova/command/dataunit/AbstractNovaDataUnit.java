package de.mario.nova.command.dataunit;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.DatatypeConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.mario.nova.Logging;
import de.mario.nova.command.util.NovaCommandUtil;
import de.mario.nova.command.util.NovaCommandUtil.DataUnitIdentifier;

/**
 *    HEADER
 * +----+--------+---------+
 * | ID | LENGTH | PAYLOAD |
 * +----+--------+---------+
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
		this.lengthBytes = NovaCommandUtil.shortToBytes(length);
	}
	
	/**
	 * @return the length of this data unit
	 */
	public short getLength() {
		return length;
	}
	
	/**
	 * Writes this data unit to the provided output stream.
	 * 
	 * @param os the {@link OutputStream}; not {@code null}
	 */
	public void writeDataUnit(final OutputStream os) {
		try {
			LOG.trace(() -> "Writing data unit");
			
			os.write(identifier.getIdentifier());
			LOG.trace(() -> "    identifier " + identifier.getIdentifier());
			
			os.write(lengthBytes);
			LOG.trace(() -> "    length bytes " + DatatypeConverter.printHexBinary(lengthBytes));
			
			final byte[] payload = getPayload();
			os.write(payload);
			LOG.trace(() -> "    payload " + DatatypeConverter.printHexBinary(payload));
		} catch (final IOException e) {
			LOG.error(() -> "Could not write dataUnit ", e);
		}
	}
	
	protected abstract byte[] getPayload();
	
	protected abstract String toStringDebug();
	
	@Override
	public String toString() {
		return toStringDebug();
	}

}
