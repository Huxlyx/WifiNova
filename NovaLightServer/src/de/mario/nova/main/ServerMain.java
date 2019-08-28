package de.mario.nova.main;

import de.mario.nova.server.NovaServer;

public class ServerMain {
	
	public static void main(final String[] args) {
		
		//TODO: assign port from args
		final int port = 7878;
		
		final NovaServer server = new NovaServer(port);
		server.run();
	}

}
