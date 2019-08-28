package de.mario.nova.command;

import de.mario.nova.command.NovaCommandUtil.LightCommand;

public abstract class AbstractNovaCommand {
	
	private final LightCommand command;
	
	protected final short origin;
	protected final short target;
	
	final byte[] cmdHeader;
	
	public AbstractNovaCommand(final short origin, final short target, final int additionalCommandLength, final LightCommand command) {
		this.origin = origin;
		this.target = target;
		this.cmdHeader = new byte[7];
		this.command = command;
		
		final short effectiveCmdLength = (short) (cmdHeader.length + additionalCommandLength);
		
		cmdHeader[0] = command.getCommandByte();
		cmdHeader[1] = (byte) (effectiveCmdLength << 8);
		cmdHeader[2] = (byte) effectiveCmdLength;
		cmdHeader[3] = (byte) (origin >> 8);
		cmdHeader[4] = (byte) origin;
		cmdHeader[5] = (byte) (target >> 8);
		cmdHeader[6] = (byte) target;
	}
	
	public short getOrigin() {
		return origin;
	}
	
	public short getTarget() {
		return target;
	}
	
	public abstract byte[] getCmdBytes();
	
	protected byte[] getCmdHeader() {
		return cmdHeader;
	}
	
	protected StringBuilder getToStringHeader() {
		final StringBuilder sb = new StringBuilder(256);
		sb.append(command.name()).append(" from ").append(origin)
		.append(" to ").append(target);
		return sb;
	}

}
