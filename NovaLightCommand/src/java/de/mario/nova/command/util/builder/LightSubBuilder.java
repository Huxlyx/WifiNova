package de.mario.nova.command.util.builder;

import de.mario.nova.command.control.NovaCommand;
import de.mario.nova.command.dataunit.DurationDataUnit;
import de.mario.nova.command.dataunit.RGBDataUnit;
import de.mario.nova.command.light.RGB;

public class LightSubBuilder {
	
	private final NovaCommand command;
	
	LightSubBuilder(final NovaCommand command) {
		this.command = command;
	}
	
	public LightSubBuilder addRGB(final int r, final int g, final int b) {
		final RGB rgb = new RGB((byte) r, (byte) g, (byte) b);
		command.addDataUnit(new RGBDataUnit(rgb));
		return this;
	}
	
	public LightSubBuilder addRGB(final Color color) {
		final RGB rgb = new RGB(color.r, color.g, color.b);
		command.addDataUnit(new RGBDataUnit(rgb));
		return this;
	}
	
	public LightSubBuilder addDuration(final short duration) {
		command.addDataUnit(new DurationDataUnit(duration));
		return this;
	}
	
	public NovaCommand finish() {
		return command;
	}

}
