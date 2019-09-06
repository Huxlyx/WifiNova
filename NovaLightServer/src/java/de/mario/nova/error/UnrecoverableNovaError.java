package de.mario.nova.error;

import java.io.IOException;

import de.mario.nova.command.dataunit.DataUnitException;

public class UnrecoverableNovaError extends IOException {

	private static final long serialVersionUID = 1L;
	
	public UnrecoverableNovaError(final String msg) {
		super(msg);
	}

	public UnrecoverableNovaError(final DataUnitException e) {
		super(e);
	}

}
