package de.mario.nova.command;

public interface NovaCommandSink {
	
	void handleCommand(final AbstractNovaCommand cmd);

}
