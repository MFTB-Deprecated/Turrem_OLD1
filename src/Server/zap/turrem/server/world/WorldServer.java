package zap.turrem.server.world;

import java.util.ArrayList;
import java.util.List;

import zap.turrem.server.entity.EntityServer;
import zap.turrem.server.entity.unit.Unit;
import zap.turrem.server.realm.RealmServer;
import zap.turrem.utils.geo.Box;

public class WorldServer
{
	public List<EntityServer> entityList = new ArrayList<EntityServer>();
	public List<RealmServer> realms = new ArrayList<RealmServer>();
	
	public void tickEntities()
	{
		for (EntityServer entity : this.entityList)
		{
			entity.onTick();
		}
	}
	
	public int addEntity(EntityServer entity)
	{
		int i = this.entityList.size();
		this.entityList.add(entity);
		return i;
	}
	
	public List<Unit> getUnitsOfRealm(RealmServer realm)
	{
		int r = realm.getId();
		List<Unit> ents = new ArrayList<Unit>();
		for (EntityServer entity : this.entityList)
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
	
	public int addRealm(RealmServer realm)
	{
		int i = this.realms.size();
		this.realms.add(realm);
		return i;
	}
	
	public List<EntityServer> getEntitiesInBox(Box box)
	{
		List<EntityServer> inbox = new ArrayList<EntityServer>();
		
		for (EntityServer e : this.entityList)
		{
			if (box.intersectsWith(e.getBoundingBox()))
			{
				inbox.add(e);
			}
		}
		
		return inbox;
	}
}
