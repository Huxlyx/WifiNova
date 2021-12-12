package de.mario.nova.server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.mario.nova.Logging;
import de.mario.nova.command.control.INovaCommandSink;
import de.mario.nova.command.control.NovaCommand;
import de.mario.nova.command.dataunit.AbstractNovaDataUnit;
import de.mario.nova.command.dataunit.DataUnitException;
import de.mario.nova.command.dataunit.DeviceIdDataUnit;
import de.mario.nova.command.dataunit.DeviceTypeDataUnit;
import de.mario.nova.command.util.ByteUtil;
import de.mario.nova.command.util.NovaCommandUtil;
import de.mario.nova.command.util.NovaCommandUtil.CommandIdentifier;
import de.mario.nova.error.UnrecoverableNovaError;

public class NovaServerRunnable implements Runnable, INovaCommandSink {

	public enum Type {

		LIGHT_CONTROLLER,

		COMMAND_CLIENT,

		UNKNOWN
	}
	
	private enum ReadResult {
		OK,
		
		DISCONNECT,
		
		ERROR
	}

	private static final Logger LOG = LogManager.getLogger(Logging.THREAD);

	private final ICommandHandler cmdHandler;
	private final String threadIdentifier;
	private final short threadId;
	private final Socket socket;
	private InputStream is;
	
	private BufferedOutputStream os;
	private boolean run = true;
	private short deviceId = Short.MIN_VALUE;

	public NovaServerRunnable(final Socket socket, final ICommandHandler controlThread, final short threadId) {
		this.socket = socket;
		this.threadId = threadId;
		this.cmdHandler = controlThread;
		this.threadIdentifier = "Thread " + threadId;
		try {
			this.is = socket.getInputStream();
			this.os = new BufferedOutputStream(socket.getOutputStream());
		} catch (final IOException e) {
			LOG.fatal(() -> "Error instantiating " + this.getClass().getSimpleName(), e);
		}
	}

	@Override
	public void run() {
		LOG.debug(() -> threadIdentifier + " started");
		try {
			final Type type = determineType();

			if (type == Type.LIGHT_CONTROLLER) {
				cmdHandler.registerCommandSink(this);
				try {
					runLightControl();
				} finally {
					cmdHandler.unregisterCommandSink(this);
				}
			} else if (type == Type.COMMAND_CLIENT) {
				runCmdClient();
			}
		} catch (final UnrecoverableNovaError e) {
			LOG.error("Error in run operation", e);
		} finally {
			close();
		}
		LOG.debug(() -> "Thread " + threadId + " terminated");
	}

	private void close() {
		try {
			if (os != null) {
				os.close();
			}
			if (is != null) {
				is.close();
			}
			socket.close();
		} catch (final IOException e) {
			LOG.error(() -> "Error closing socket", e);
		}
	}

	private Type determineType() throws UnrecoverableNovaError {

		final NovaCommand cmd = getCommand();

		if (cmd == null) {
			throw new UnrecoverableNovaError("Could not determine device type, command is null");
		}
		if (cmd.getCommandIdentifier() != CommandIdentifier.HANDSHAKE) {
			throw new UnrecoverableNovaError("Expected handshake but got " + cmd.getCommandIdentifier());
		}

		final List<AbstractNovaDataUnit> dataUnits = cmd.getDataUnits();

		if (dataUnits.size() != 2) {
			throw new UnrecoverableNovaError("Expected two data units (type + id) but got " + dataUnits.size());
		}

		final DeviceTypeDataUnit deviceType = (DeviceTypeDataUnit) dataUnits.get(0);
		final DeviceIdDataUnit devId = (DeviceIdDataUnit) dataUnits.get(1);

		LOG.debug(() -> threadIdentifier + " identified as " + deviceType.getDeviceType() + " with device id " + devId.getDeviceId());
		
		switch (deviceType.getDeviceType()) {
		case COMMAND_CLIENT:
			return Type.COMMAND_CLIENT;
		case LIGHT_CONTROLLER:
			this.deviceId = devId.getDeviceId();
			return Type.LIGHT_CONTROLLER;
		default:
			LOG.warn(() -> "could not determine type of thread " + threadId);
			return Type.UNKNOWN;
		}
	}

	private void runCmdClient() throws UnrecoverableNovaError {
		while (run) {
			final NovaCommand cmd = getCommand();
			if (cmd == null) {
				throw new UnrecoverableNovaError("Could not handle command. Unrecoverable error, terminating client");
			} else if (cmd.getCommandIdentifier() == CommandIdentifier.DISCONNECT) {
				LOG.debug(() -> "Detected client disconnect");
				run = false;
			} else {
				cmdHandler.queueCommand(cmd);
			}
		}
	}

	private void runLightControl() {
		final byte[] bytes = new byte[3];
		while (run) {
			if (readBytes(bytes) == ReadResult.DISCONNECT) {
				break;
			}
			LOG.error(threadIdentifier + " received bytes but didn't expect any");
		}
	}

	private NovaCommand getCommand() throws UnrecoverableNovaError {

		/* read header */
		final byte[] header = new byte[3];
		switch (readBytes(header)) {
		case OK:
			/* everything ok, just continue */
			break;
		case DISCONNECT:
			return NovaCommand.disconnect();
		case ERROR:
			return null;
		default:
			throw new IllegalArgumentException("Unexpected read result");
		}

		final CommandIdentifier commandId = CommandIdentifier.fromByte(header[0]);
		final short commandLength = ByteUtil.bytesToShort(header[1], header[2]);
		LOG.debug(() -> "Got new " + commandId + " with length " + commandLength);

		/* read payload */
		final byte[] payload = new byte[commandLength];
		if (readBytes(payload) != ReadResult.OK) {
			return null;
		}

		try {
			final NovaCommand cmd = NovaCommandUtil.composeFromBytes(commandId, payload);
			LOG.trace(() -> "Got " + cmd);
			return cmd;
		} catch (final DataUnitException e) {
			throw new UnrecoverableNovaError(e);
		}
	}

	private ReadResult readBytes(final byte[] payload) {
		int bytesRead = 0;
		int read;
		LOG.trace(() -> "Reading " + payload.length + " bytes"); 
		try {
			while ((read = is.read(payload, bytesRead, payload.length - bytesRead)) > 0) {
				bytesRead += read;
				final int logRead = read;
				LOG.trace(() -> "Read " + logRead + " bytes"); 
			}

			if (read < 0) {
				LOG.debug(() -> "read returned -1");
				run = false;
				return ReadResult.DISCONNECT;
			}
		} catch (final IOException e) {
			LOG.error(() -> "Read error", e);
			run = false;
			return ReadResult.ERROR;
		}
		return ReadResult.OK;
	}


	@Override
	public void handleDataUnit(final AbstractNovaDataUnit cmd) {
		try {
			cmd.writeDataUnit(os);
			os.flush();
			LOG.debug(() -> "Sent cmd bytes for cmd " + cmd);
		} catch (IOException e) {
			LOG.error(() -> "Could not send data unit", e);
		}
	}

	@Override
	public short getId() {
		return deviceId;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(128);
		sb.append("Runnable ").append(threadIdentifier).append(" id: ").append(deviceId);
		sb.append(" running? ").append(run ? "yes!" : "no!");
		return sb.toString();
	}

}
