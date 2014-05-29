package net.turrem.server.world;

import net.turrem.server.Realm;
import net.turrem.server.network.GameConnection;
import net.turrem.server.network.client.ClientPacket;

public class ClientPlayer
{
	public World theWorld;
	public String username;
	public GameConnection theConnection;
	public Realm theRealm;
	
	public ClientPlayer(World world, String name)
	{
		this.theWorld = world;
		this.username = name;
	}
	
	public void joinNetwork(GameConnection connection)
	{
		this.theConnection = connection;
	}
	
	public void exit()
	{
		if (this.theRealm != null)
		{
			this.theRealm.onPlayerExit();
		}
	}
	
	public void processPacket(ClientPacket packet)
	{
		
	}
}
