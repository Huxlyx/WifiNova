package de.mario.nova.server;

import de.mario.nova.command.control.INovaCommandSink;
import de.mario.nova.command.control.NovaCommand;

public interface ICommandHandler {
	
	void queueCommand(final NovaCommand cmd);
	
	void registerCommandSink(final INovaCommandSink cmdSink);
	
	void unregisterCommandSink(final INovaCommandSink cmdSink);

}
