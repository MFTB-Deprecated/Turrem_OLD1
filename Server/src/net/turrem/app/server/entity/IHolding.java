package net.turrem.app.server.entity;

import net.turrem.app.server.Realm;

public interface IHolding
{
	public Realm getAllegiance();
	
	public Realm setAllegiance(Realm newrealm);
	
	public boolean getRealmVisibility(int x, int z);
	
	public boolean getThisVisibility(int x, int z);
}
