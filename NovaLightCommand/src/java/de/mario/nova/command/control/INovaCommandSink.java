package de.mario.nova.command.control;

import de.mario.nova.command.dataunit.AbstractNovaDataUnit;

public interface INovaCommandSink {
	
	void handleDataUnit(final AbstractNovaDataUnit cmd);
	
	short getId();

}
