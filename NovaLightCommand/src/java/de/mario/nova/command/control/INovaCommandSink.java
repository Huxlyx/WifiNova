package de.mario.nova.command.control;

import de.mario.nova.command.dataunit.AbstractNovaDataUnit;

public interface INovaCommandSink {
	
	void handleCommand(final AbstractNovaDataUnit cmd);

}
