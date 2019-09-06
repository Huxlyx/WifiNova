package de.mario.nova.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.mario.nova.Logging;

public class NovaServer {
	
	private static final  Logger LOG = LogManager.getLogger(Logging.SERVER);
	
	short threadId = 0;
	private final int port;
	
	private AtomicBoolean runServer = new AtomicBoolean(true);
	
	public NovaServer(final int port) {
		this.port = port;
	}
	
	public void run() {
		final Runnable controlRunnable = new ControlRunnable();
		final Thread controlThread = new Thread(controlRunnable);
		controlThread.start();
		
		LOG.debug(() -> "Server startup on port " + port);
		try (final ServerSocket serverSocket = new ServerSocket(port)) {
			while (runServer.get()) {
				final Socket client = serverSocket.accept();
				LOG.info(() -> "Got new client connection ");
				final Thread thread = new Thread(new NovaServerRunnable(client, (ICommandHandler) controlRunnable, threadId++));
				thread.start();
			}
		} catch (final IOException e) {
			LOG.fatal(() -> "I/O Socket error", e); 
		}
	}
	
	public void setRunServer(final boolean run) {
		LOG.trace(() -> "setting runServer to " + run); 
		runServer.set(run);
	}

}
