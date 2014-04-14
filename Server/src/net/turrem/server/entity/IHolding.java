package net.turrem.server.entity;

import net.turrem.server.Realm;

public interface IHolding
{
	public Realm getAllegiance();
	
	public Realm setAllegiance(Realm newrealm);
}
