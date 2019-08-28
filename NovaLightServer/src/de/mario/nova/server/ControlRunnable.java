package de.mario.nova.server;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.mario.nova.Logging;
import de.mario.nova.command.AbstractNovaCommand;
import de.mario.nova.command.NovaCommandSink;

public class ControlRunnable implements Runnable, CommandHandler {

	private static final Logger LOG = LogManager.getLogger(Logging.CONTROL);

	private static final BlockingQueue<AbstractNovaCommand> COMMAND_QUEUE = new ArrayBlockingQueue<>(512);
	private static final BlockingQueue<NovaCommandSink> CMD_SINKS = new ArrayBlockingQueue<>(64);

	private boolean run = true;

	@Override
	public void run() {
		LOG.info(() -> "Started control loop"); 

		while (run) {
			try {
				final AbstractNovaCommand cmd = COMMAND_QUEUE.take();

				if (cmd.getTarget() == Short.MAX_VALUE) {
					/* broadcast */
					LOG.trace(() -> "Broadcast command " + cmd);
					CMD_SINKS.forEach(s -> s.handleCommand(cmd));
				}
			} catch (final InterruptedException e) {
				LOG.warn(() -> "Main control loop interrupted", e);
				Thread.currentThread().interrupt();
			}
		}
	}

	@Override
	public void queueCommand(final AbstractNovaCommand command) {
		if (COMMAND_QUEUE.offer(command)) {
			LOG.trace(() -> "Queued new command " + command); 
		} else {
			LOG.warn(() -> "Queuing command " + command + " failed");
		}
	}

	@Override
	public void registerCommandSink(final NovaCommandSink cmdSink) {
		if (CMD_SINKS.add(cmdSink)) {
			LOG.debug(() -> "Added cmd sink " + cmdSink);
		} else {
			LOG.warn(() -> "Could not add cmd sink " + cmdSink);
		}
	}

	@Override
	public void unregisterCommandSink(final NovaCommandSink cmdSink) {
		if (CMD_SINKS.remove(cmdSink)) {
			LOG.debug(() -> "Unregistered cmd sink " + cmdSink);
		} else {
			LOG.warn(() -> "Could not unregister " + cmdSink);
		}
	}

}
