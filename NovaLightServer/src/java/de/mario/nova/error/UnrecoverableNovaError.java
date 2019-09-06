package de.mario.nova.error;

import java.io.IOException;

public class UnrecoverableNovaError extends IOException {

	private static final long serialVersionUID = 1L;
	
	public UnrecoverableNovaError(final String msg) {
		super(msg);
	}

}
