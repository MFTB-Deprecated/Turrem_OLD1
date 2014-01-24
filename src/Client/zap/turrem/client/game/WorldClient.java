package zap.turrem.client.game;

import java.util.ArrayList;
import java.util.List;

import zap.turrem.client.game.entity.EntityClient;
import zap.turrem.client.render.RenderWorld;
import zap.turrem.utils.geo.Box;

public class WorldClient
{
	public List<EntityClient> entityList = new ArrayList<EntityClient>();
	public List<RealmClient> realms = new ArrayList<RealmClient>();
	
	public RenderWorld theRender;
	
	public WorldClient(RenderWorld render)
	{
		this.theRender = render;
	}

	public void tickWorld()
	{
		this.tickEntities();
	}

	public void tickEntities()
	{
		for (int i = 0; i < this.entityList.size(); i++)
		{
			EntityClient e = this.entityList.get(i);
			if (e.isDead || !e.isAppear)
			{
				this.entityList.remove(i--);
			}
			else
			{
				e.onTick();
			}
		}
	}

	public void onlyTickEntities()
	{
		for (EntityClient e : this.entityList)
		{
			e.onTick();
		}
	}

	public void cleanEntities()
	{
		for (int i = 0; i < this.entityList.size(); i++)
		{
			EntityClient e = this.entityList.get(i);
			if (e.isDead || !e.isAppear)
			{
				this.entityList.remove(i--);
			}
		}
	}
	
	public void render()
	{
		this.theRender.render();
	}
	
	public List<EntityClient> getEntitiesHit(Box box)
	{
		List<EntityClient> hit = new ArrayList<EntityClient>();
		for (EntityClient e : this.entityList)
		{
			if (box.intersectsWith(e.getBoundingBox()))
			{
				hit.add(e);
			}
		}
		return hit;
	}
}
