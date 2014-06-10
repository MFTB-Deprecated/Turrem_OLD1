package net.turrem.server;

import java.util.ArrayList;

import net.turrem.server.entity.IHolding;
import net.turrem.server.world.ClientPlayer;
import net.turrem.server.world.World;

public class Realm
{
	private static int nextRealmId = 0;
	public final String user;
	public World theWorld;	
	public ArrayList<IHolding> holdings = new ArrayList<IHolding>();
	private ClientPlayer client = null;
	public final int realmId;
	
	public Realm(String username, World world)
	{
		this.theWorld = world;
		world.realms.put(username, this);
		this.user = username;
		this.realmId = nextRealmId++;
		this.spawn();
	}
	
	public void spawn()
	{
		
	}
	
	public void joinRealm(IHolding item)
	{
		if (!this.holdings.contains(item))
		{
			this.holdings.add(item);
		}
	}
	
	public void leaveRealm(IHolding item, String reason)
	{
		this.holdings.remove(item);
	}
	
	public ClientPlayer getClient()
	{
		return client;
	}

	public void setClient(ClientPlayer client)
	{
		this.client = client;
		this.client.theRealm = this;
	}
	
	public void onPlayerExit()
	{
		this.client = null;
	}
	
	public void setVisibility(int chunkx, int chunkz, boolean visible)
	{
		this.theWorld.storage.setVisibility(chunkx, chunkz, this.realmId, visible);
	}

	public boolean getVisibility(int chunkx, int chunkz)
	{
		return this.theWorld.storage.getVisibility(chunkx, chunkz, this.realmId);
	}
	
	public void setVisibilityAt(int x, int z, boolean visible)
	{
		this.theWorld.storage.setVisibility(x >> 4, z >> 4, this.realmId, visible);
	}

	public boolean getVisibilityAt(int x, int z)
	{
		return this.theWorld.storage.getVisibility(x >> 4, z >> 4, this.realmId);
	}
}
