package de.mario.nova.server;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.mario.nova.Logging;
import de.mario.nova.command.control.INovaCommandSink;
import de.mario.nova.command.control.NovaCommand;
import de.mario.nova.command.dataunit.AbstractNovaDataUnit;
import de.mario.nova.command.dataunit.BroadcastDataUnit;
import de.mario.nova.command.dataunit.CommandCountDataUnit;
import de.mario.nova.command.dataunit.DeviceIdDataUnit;

public class ControlRunnable implements Runnable, ICommandHandler {

	private static final Logger LOG = LogManager.getLogger(Logging.CONTROL);

	private static final BlockingQueue<NovaCommand> COMMAND_QUEUE = new ArrayBlockingQueue<>(512);
	private static final BlockingQueue<INovaCommandSink> CMD_SINKS = new ArrayBlockingQueue<>(64);

	private AtomicBoolean run = new AtomicBoolean(true);

	@Override
	public void run() {
		LOG.info(() -> "Started control loop"); 

		while (run.get()) {
			try {
				final NovaCommand cmd = COMMAND_QUEUE.take();
				final List<AbstractNovaDataUnit> dataUnits = cmd.getDataUnits();
				
				if (dataUnits.size() < 2) {
					LOG.warn(() -> "Expected at least 2 data units, but got " + dataUnits.size() + ". Ignoring command " + cmd);
					continue;
				}
				
				final AbstractNovaDataUnit target = dataUnits.get(0);
				
				if (target instanceof BroadcastDataUnit) {
					LOG.debug(() -> "Broadcast " + (dataUnits.size() - 1) + " data units");
					if (dataUnits.size() > 2) {
						final CommandCountDataUnit ccdu = new CommandCountDataUnit((short) (dataUnits.size() - 1));
						CMD_SINKS.forEach(s -> s.handleDataUnit(ccdu));
					}
					dataUnits.stream().skip(1).forEach(du -> CMD_SINKS.forEach(s -> s.handleDataUnit(du)));
				} else if (target instanceof DeviceIdDataUnit) {
					throw new IllegalStateException("Targeted data units are not implemented yet");
				}
			} catch (final InterruptedException e) {
				LOG.warn(() -> "Main control loop interrupted", e);
				Thread.currentThread().interrupt();
			}
		}
	}
	
	public void requestStop() {
		LOG.debug(() -> "Requested stop on control runnable"); 
		run.set(false);
	}

	@Override
	public void queueCommand(NovaCommand cmd) {
		if (COMMAND_QUEUE.offer(cmd)) {
			LOG.trace(() -> "Queued new command " + cmd); 
		} else {
			LOG.warn(() -> "Queuing command " + cmd + " failed");
		}
	}

	@Override
	public void registerCommandSink(final INovaCommandSink cmdSink) {
		if (CMD_SINKS.add(cmdSink)) {
			LOG.debug(() -> "Added cmd sink " + cmdSink);
		} else {
			LOG.warn(() -> "Could not add cmd sink " + cmdSink);
		}
	}

	@Override
	public void unregisterCommandSink(final INovaCommandSink cmdSink) {
		if (CMD_SINKS.remove(cmdSink)) {
			LOG.debug(() -> "Unregistered cmd sink " + cmdSink);
		} else {
			LOG.warn(() -> "Could not unregister " + cmdSink);
		}
	}

}
