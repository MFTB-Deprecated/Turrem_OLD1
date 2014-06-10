package net.turrem.server.entity;

import net.turrem.server.Realm;

public interface IHolding
{
	public Realm getAllegiance();
	
	public Realm setAllegiance(Realm newrealm);
	
	public boolean getRealmVisibility(int x, int z);
	
	public boolean getThisVisibility(int x, int z);
}
