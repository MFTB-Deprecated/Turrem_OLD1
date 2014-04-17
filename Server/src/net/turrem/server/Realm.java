package net.turrem.server;

import java.util.ArrayList;

import net.turrem.server.entity.IHolding;
import net.turrem.server.world.World;

public class Realm
{
	public final String user;
	public World theWorld;	
	public ArrayList<IHolding> holdings = new ArrayList<IHolding>();
	
	public Realm(String username, World world)
	{
		this.theWorld = world;
		world.realms.put(username, this);
		this.user = username;
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
}
