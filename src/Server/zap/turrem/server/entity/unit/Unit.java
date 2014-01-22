package zap.turrem.server.entity.unit;

import zap.turrem.server.entity.Entity;
import zap.turrem.server.realm.RealmMP;
import zap.turrem.server.world.World;

public class Unit extends Entity
{
	protected RealmMP thisRealm;
	
	public Unit(World world)
	{
		super(world);
	}
	
	public void setRealm(RealmMP realm)
	{
		this.thisRealm = realm;
		this.thisRealm.addUnit(this);
	}

	public final RealmMP getRealm()
	{
		return thisRealm;
	}
}
