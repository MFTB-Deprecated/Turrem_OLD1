package net.turrem.server.load;

import net.turrem.server.TurremServer;

public class GameLoader
{
	protected TurremServer theServer;
	protected EntityLoader loaderEntity;
	
	public GameLoader(TurremServer server)
	{
		this.theServer = server;
		this.loaderEntity = new EntityLoader(this);
	}
}
