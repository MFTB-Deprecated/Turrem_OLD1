package zap.turrem.server.entity.unit;

import zap.turrem.server.entity.EntityServer;
import zap.turrem.server.realm.RealmServer;
import zap.turrem.server.world.WorldServer;

public class Unit extends EntityServer
{
	protected RealmServer thisRealm;
	
	public Unit(WorldServer world)
	{
		super(world);
	}
	
	public void setRealm(RealmServer realm)
	{
		this.thisRealm = realm;
		this.thisRealm.addUnit(this);
	}

	public final RealmServer getRealm()
	{
		return thisRealm;
	}
}
