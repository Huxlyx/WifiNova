package de.mario.nova.command.util.builder;

import de.mario.nova.command.control.NovaCommand;
import de.mario.nova.command.dataunit.BroadcastDataUnit;
import de.mario.nova.command.util.NovaCommandUtil.CommandIdentifier;

public class NovaLightCommandBuilder {
	
	private final NovaCommand command;
	
	private NovaLightCommandBuilder() {
		this.command = new NovaCommand(CommandIdentifier.LIGHT_COMMAND);
	}
	
	public static NovaLightCommandBuilder newCommand() {
		return new NovaLightCommandBuilder();
	}
	
	public LightSubBuilder asBroadcast() {
		command.addDataUnit(new BroadcastDataUnit());
		return new LightSubBuilder(command);
	}
	
	public NovaCommand finish() {
		return command;
	}

}
