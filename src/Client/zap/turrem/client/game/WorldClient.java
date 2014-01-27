package zap.turrem.client.game;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import units.turrem.Eekysam;
import zap.turrem.client.Turrem;
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
	
	public EntityClient picked = null;
	
	private int spawntimer = 0;
	
	public WorldClient(RenderWorld render, Game game)
	{
		this.theRender = render;
		this.theGame = game;
	}

	public void tickWorld()
	{
		this.tickKeys();
		this.tickEntities();
		this.picked = this.getEntityPicked();
	}
	
	public void tickKeys()
	{
		if (this.spawntimer > 0)
		{
			this.spawntimer--;
		}
		while (Keyboard.next())
		{
			if (Keyboard.getEventKey() == Keyboard.KEY_S && this.spawntimer == 0)
			{
				Point g = this.theGame.face.getPickGround();
				if (g != null)
				{
					g.yCoord = 0.0D;
					EntityClient ent = new EntityClient(new Eekysam());
					ent.push(this.theGame.theWorld, Turrem.getTurrem().theRender);
					ent.setPosition(g.xCoord, g.yCoord, g.zCoord);
					this.spawntimer = 20;
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_K)
			{
				if (this.picked != null)
				{
					this.picked.kill();
				}
			}
		}
		while (Mouse.next())
		{
			int b = Mouse.getEventButton();
			if (b == 0)
			{
				if (this.picked != null)
				{
					this.picked.setSelected(true);
				}
				else
				{
					EntityClient.deselect = true;
				}
			}
			if (b == 1)
			{
				Point g = this.theGame.face.getPickGround();
				if (g != null)
				{
					g.yCoord = 0.0D;
				}
				this.theGame.theWorld.moveTo = g;
			}
		}
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
				this.moveTo.xCoord += e.getBoundingBox().getXLength();
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
	
	private EntityClient getEntityPicked()
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
