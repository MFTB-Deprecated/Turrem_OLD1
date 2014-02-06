package zap.turrem.client.game.world;

import java.util.ArrayList;
import java.util.List;

import zap.turrem.client.game.Game;
import zap.turrem.client.game.RealmClient;
import zap.turrem.client.game.entity.EntityClient;
import zap.turrem.client.game.entity.IEntitySelectable;
import zap.turrem.client.game.operation.Operation;
import zap.turrem.client.game.select.SelectionEvent;
import zap.turrem.utils.geo.Box;
import zap.turrem.utils.geo.BoxPin;
import zap.turrem.utils.geo.Point;
import zap.turrem.utils.geo.Ray;

public class WorldClient
{
	public List<EntityClient> entityList = new ArrayList<EntityClient>();
	public List<RealmClient> realms = new ArrayList<RealmClient>();

	public List<SelectionEvent> selectionEvents = new ArrayList<SelectionEvent>();
	public List<Operation> operations = new ArrayList<Operation>();

	public Game theGame;
	
	private EntityClient pickedEntity = null;

	public WorldClient(Game game)
	{
		this.theGame = game;
	}
	
	public EntityClient getEntityPicked()
	{
		return this.pickedEntity;
	}

	public void tickWorld()
	{
		this.tickEntities();
		this.pickedEntity = this.calculateEntityPicked();
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

			if (e instanceof IEntitySelectable)
			{
				IEntitySelectable es = (IEntitySelectable) e;
				for (SelectionEvent sel : this.selectionEvents)
				{
					es.runSelect(sel);
				}
				if (es.isSelected())
				{
					for (Operation op : this.operations)
					{
						es.doOperation(op);
					}
				}
			}
		}

		this.selectionEvents.clear();
		this.operations.clear();
	}

	public void render()
	{
		for (EntityClient e : this.entityList)
		{
			e.render();
		}
	}

	public EntityClient calculateEntityPicked()
	{
		Ray r = this.theGame.getPickRay();
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
	
	public List<EntityClient> getEntitiesHit(Box box, EntityClient exclude)
	{
		List<EntityClient> hit = new ArrayList<EntityClient>();
		long skip = exclude.uid;
		for (EntityClient e : this.entityList)
		{
			if (e.uid != skip && box.intersectsWith(e.getBoundingBox()))
			{
				hit.add(e);
			}
		}
		return hit;
	}
}
