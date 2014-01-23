package zap.turrem.server.world;

import java.util.ArrayList;
import java.util.List;

import zap.turrem.server.entity.EntityMP;
import zap.turrem.server.entity.unit.Unit;
import zap.turrem.server.realm.RealmMP;

public class World
{
	public List<EntityMP> entityList = new ArrayList<EntityMP>();
	public List<RealmMP> realms = new ArrayList<RealmMP>();
	
	public void tickEntities()
	{
		for (EntityMP entity : this.entityList)
		{
			entity.onTick();
		}
	}
	
	public int addEntity(EntityMP entity)
	{
		int i = this.entityList.size();
		this.entityList.add(entity);
		return i;
	}
	
	public List<Unit> getUnitsOfRealm(RealmMP realm)
	{
		int r = realm.getId();
		List<Unit> ents = new ArrayList<Unit>();
		for (EntityMP entity : this.entityList)
		{
			if (entity instanceof Unit)
			{
				Unit unit = (Unit) entity;
				if (unit.getRealm().getId() == r)
				{
					ents.add(unit);
				}
			}
		}
		return ents;
	}
	
	public int addRealm(RealmMP realm)
	{
		int i = this.realms.size();
		this.realms.add(realm);
		return i;
	}
}
