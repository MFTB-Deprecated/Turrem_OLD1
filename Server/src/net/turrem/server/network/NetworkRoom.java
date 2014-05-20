package net.turrem.server.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.turrem.server.TurremServer;

public class NetworkRoom
{
	protected TurremServer theTurrem;
	public ServerWelcomeManager welcome;
	private List<GameConnection> clients;
	
	public static int clientLimitPerTick = 1000;
	
	public NetworkRoom(TurremServer turrem)
	{
		this.theTurrem = turrem;
		this.clients = Collections.synchronizedList(new ArrayList<GameConnection>());
		this.welcome = new ServerWelcomeManager(this.theTurrem, this);
		this.welcome.start();
	}
	
	public boolean addClient(GameConnection client)
	{
		String name = client.name;
		for (GameConnection c : this.clients)
		{
			if (c.name.equalsIgnoreCase(name))
			{
				return false;
			}
		}
		this.clients.add(client);
		return true;
	}
	
	public void networkTick()
	{
        for (int i = 0; i < this.clients.size(); ++i)
        {
        	GameConnection con = this.clients.get(i);
        	if (con.isRunning())
        	{
        		con.processPackets(clientLimitPerTick);
        	}
        	else
        	{
        		this.clients.remove(i--);
        	}
        }
	}
}