package de.mario.nova.command.util.builder;

import java.io.IOException;
import java.io.OutputStream;

import de.mario.nova.command.control.NovaCommand;
import de.mario.nova.command.dataunit.FlickerDataUnit;
import de.mario.nova.command.dataunit.LEDTestDataUnit;
import de.mario.nova.command.dataunit.RGBDataUnit;
import de.mario.nova.command.light.RGB;

public class LightSubBuilder {
	
	private final NovaCommand command;
	
	LightSubBuilder(final NovaCommand command) {
		this.command = command;
	}
	
	public LightSubBuilder addFlicker(final int r, final int g, final int b) {
		final RGB rgb = new RGB((byte) r, (byte) g, (byte) b);
		command.addDataUnit(new FlickerDataUnit(rgb));
		return this;
	}
	
	public LightSubBuilder addFlicker(final Color color) {
		final RGB rgb = new RGB(color.r, color.g, color.b);
		command.addDataUnit(new FlickerDataUnit(rgb));
		return this;
	}
	
	public LightSubBuilder addStaticColor(final int r, final int g, final int b) {
		final RGB rgb = new RGB((byte) r, (byte) g, (byte) b);
		command.addDataUnit(new RGBDataUnit(rgb));
		return this;
	}
	
	public LightSubBuilder addStaticColor(final Color color) {
		final RGB rgb = new RGB(color.r, color.g, color.b);
		command.addDataUnit(new RGBDataUnit(rgb));
		return this;
	}
	
	public LightSubBuilder addLEDTest() {
		command.addDataUnit(new LEDTestDataUnit());
		return this;
	}
	
	public GradientSubBuilder addGradient() {
		return new GradientSubBuilder(this);
	}
	
	public void go(final OutputStream os) throws IOException {
		command.writeCommand(os);
	}

}
