package zap.turrem.server.world;

import java.util.ArrayList;
import java.util.List;

import zap.turrem.server.entity.Entity;
import zap.turrem.server.entity.unit.Unit;
import zap.turrem.server.realm.RealmMP;

public class World
{
	public List<Entity> entityList = new ArrayList<Entity>();
	public List<RealmMP> realms = new ArrayList<RealmMP>();
	
	public void tickEntities()
	{
		for (Entity entity : this.entityList)
		{
			entity.onTick();
		}
	}
	
	public int addEntity(Entity entity)
	{
		int i = this.entityList.size();
		this.entityList.add(entity);
		return i;
	}
	
	public List<Unit> getUnitsOfRealm(RealmMP realm)
	{
		int r = realm.getId();
		List<Unit> ents = new ArrayList<Unit>();
		for (Entity entity : this.entityList)
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
