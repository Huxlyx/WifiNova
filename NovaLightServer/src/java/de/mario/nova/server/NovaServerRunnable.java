package de.mario.nova.server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.mario.nova.Logging;
import de.mario.nova.command.AbstractNovaCommand;
import de.mario.nova.command.NovaCommandSink;
import de.mario.nova.command.NovaCommandUtil;
import de.mario.nova.command.StaticLightCommand;

public class NovaServerRunnable implements Runnable, NovaCommandSink {

	public enum Type {

		LIGHT_CONTROLLER,

		COMMAND_CLIENT,

		UNKNOWN
	}

	private static final Logger LOG = LogManager.getLogger(Logging.THREAD);

	private final CommandHandler cmdHandler;

	private final String threadIdentifier;

	private final short threadId;

	private final Socket socket;
	private InputStream is;
	private BufferedOutputStream os;

	private boolean run = true;

	public NovaServerRunnable(final Socket socket, final CommandHandler controlThread, final short threadId) {
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
		final Type type = determineType();

		if (type == Type.LIGHT_CONTROLLER) {
			cmdHandler.registerCommandSink(this);
			runLightControl();
		} else if (type == Type.COMMAND_CLIENT) {
			runCmdClient();
		}

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

		if (type == Type.LIGHT_CONTROLLER) {
			cmdHandler.unregisterCommandSink(this);
		}

		LOG.debug(() -> "Thread " + threadId + " terminated");
	}

	private Type determineType() {
		final byte[] bytes = new byte[4];
		int bytesRead = 0;
		int read;
		try {
			while ((read = is.read(bytes, bytesRead, bytes.length - bytesRead)) > 0) {
				bytesRead += read;
			}

			if (bytes[0] == NovaCommandUtil.HANDHSKAE) {
				LOG.debug(() -> threadIdentifier + " got handshake"); 
				switch (bytes[1]) {
				case NovaCommandUtil.COMMAND_CLIENT:
					LOG.debug(() -> threadIdentifier + " identified as COMMAND_CLIENT"); 
					return Type.COMMAND_CLIENT;
				case NovaCommandUtil.LIGHT_CONTROLLER:
					LOG.debug(() -> threadIdentifier + " identified as LIGHT_CONTROLLER"); 
					return Type.LIGHT_CONTROLLER;
				default: 
					throw new IllegalStateException("Unkown identifier type " + bytes[1]);
				}
			}
		} catch (final IOException e) {
			LOG.error(() -> "Read error", e);
		}
		LOG.warn(() -> "could not determine type of thread " + threadId);
		return Type.UNKNOWN;
	}

	private void runCmdClient() {
		final byte[] bytes = new byte[8];
		while (run) {
			if ( ! readBytes(bytes)) {
				break;
			}
			final int result = 	
					((bytes[0] & 0xFF) << 24) 	| 
					((bytes[1] & 0xFF) << 16) 	| 
					((bytes[2] & 0xFF) << 8 ) 	| 
					((bytes[3] & 0xFF) << 0 );
			LOG.trace(() -> "got " + result + " " + Integer.toHexString(result).toUpperCase());
			cmdHandler.queueCommand(new StaticLightCommand(threadId, Short.MAX_VALUE, Arrays.copyOfRange(bytes, 5, 8)));
		}
	}

	private void runLightControl() {
		final byte[] bytes = new byte[4];
		while (run) {
			if ( ! readBytes(bytes)) {
				break;
			}
		}
	}

	private boolean readBytes(final byte[] bytes) {
		int bytesRead = 0;
		int read;
		try {
			while ((read = is.read(bytes, bytesRead, bytes.length - bytesRead)) > 0) {
				bytesRead += read;
			}

			if (read < 0) {
				LOG.debug(() -> "read returned -1");
				run = false;
				return false;
			}
		} catch (IOException e) {
			LOG.error(() -> "Read error", e);
			run = false;
			return false;
		}
		return true;
	}

	@Override
	public void handleCommand(final AbstractNovaCommand cmd) {
		try {
			os.write(cmd.getCmdBytes());
			os.flush();
			LOG.trace(() -> "Sent cmd bytes for cmd " + cmd);
		} catch (IOException e) {
			LOG.error(() -> "Could not send command", e);
		}
	}

}
