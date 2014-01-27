package zap.turrem.client.game;

import java.util.ArrayList;
import java.util.List;

import zap.turrem.client.game.entity.EntityClient;
import zap.turrem.client.render.RenderWorld;
import zap.turrem.utils.geo.Box;
import zap.turrem.utils.geo.BoxPin;
import zap.turrem.utils.geo.Point;
import zap.turrem.utils.geo.Ray;

public class WorldClient
{
	public List<EntityClient> entityList = new ArrayList<EntityClient>();
	public List<RealmClient> realms = new ArrayList<RealmClient>();
	
	public RenderWorld theRender;
	
	public Game theGame;

	public Point moveTo = null;
	
	public WorldClient(RenderWorld render, Game game)
	{
		this.theRender = render;
		this.theGame = game;
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
			if (e.isSelected() && this.moveTo != null)
			{
				e.setMotion(e.getLocation(), this.moveTo, 100);
			}
		}
		this.moveTo = null;
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
	
	public EntityClient getEntityPicked()
	{
		Ray r = this.theGame.face.getPickRay();
		List<EntityClient> ents = this.getEntitiesHit(r.getBox());
		EntityClient returne = null;
		float dist = (float) r.getLengthSqr();
		for (EntityClient e : ents)
		{
			BoxPin pin = e.getBoundingBox().calculateIntercept(r);
			if (pin != null)
			{
				float d = (float) Point.squareDistance(r.start, pin.location);
				if (d < dist)
				{
					dist = d;
					returne = e;
				}
			}
		}
		return returne;
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
