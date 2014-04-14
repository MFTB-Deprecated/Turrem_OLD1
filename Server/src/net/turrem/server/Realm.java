package net.turrem.server;

import java.util.ArrayList;

import net.turrem.server.entity.IHolding;

public class Realm
{
	public final String user;
	
	public ArrayList<IHolding> holdings = new ArrayList<IHolding>();
	
	public Realm(String username)
	{
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
