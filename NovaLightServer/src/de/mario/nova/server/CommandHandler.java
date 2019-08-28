package de.mario.nova.server;

import de.mario.nova.command.AbstractNovaCommand;
import de.mario.nova.command.NovaCommandSink;

public interface CommandHandler {
	
	void queueCommand(final AbstractNovaCommand cmd);
	
	void registerCommandSink(final NovaCommandSink cmdSink);
	
	void unregisterCommandSink(final NovaCommandSink cmdSink);

}
