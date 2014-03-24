package zap.turrem.client.game.world;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import turrem.Citizen;
import zap.turrem.client.Turrem;
import zap.turrem.client.game.Game;
import zap.turrem.client.game.RealmClient;
import zap.turrem.client.render.engine.RenderEngine;
import zap.turrem.core.entity.Entity;
import zap.turrem.core.entity.IEntityAmbient;
import zap.turrem.utils.geo.Box;
import zap.turrem.utils.geo.BoxPin;
import zap.turrem.utils.geo.Point;
import zap.turrem.utils.geo.Ray;

public class WorldClient
{
	private int viewage = 0;

	public List<Entity> entityList = new ArrayList<Entity>();
	public List<RealmClient> realms = new ArrayList<RealmClient>();

	public Game theGame;
	protected Turrem theTurrem;

	private Entity pickedEntity = null;

	public WorldTerrainGen terrain;

	public RenderEngine terrainRender;

	public ArrayList<Chunk> chunks = new ArrayList<Chunk>();

	public float rendercount = 0.0F;

	public WorldClient(Game game, Turrem turrem)
	{
		this.theGame = game;
		this.theTurrem = turrem;
		this.terrain = new WorldTerrainGen(turrem.worldseed);
		this.terrain.generate();
		this.terrainRender = new RenderEngine();

		Citizen e = new Citizen();
		e.setPosition(0.0F, this.scaleWorld(1), 0.0F);
		e.push(this, turrem.theRender);
	}

	public void updateChunks(int maxop, int r)
	{
		int d = r * 2 + 1;
		Point cam = this.theGame.getFace().getFocus();
		double sc = this.scaleWorld(16);
		int camx = (int) (cam.xCoord / sc);
		int camz = (int) (cam.zCoord / sc);
		if (cam.xCoord < 0)
		{
			camx--;
		}
		if (cam.zCoord < 0)
		{
			camz--;
		}
		boolean[] chunks = new boolean[d * d];
		for (Chunk c : this.chunks)
		{
			if (Math.abs(c.chunkx - camx) > r || Math.abs(c.chunky - camz) > r)
			{
				if (c.isLoaded())
				{
					c.unloadModel();
				}
			}
			else
			{
				chunks[((c.chunkx - camx + r) % d) + ((c.chunky - camz + r) % d) * d] = true;
				if (!c.isLoaded())
				{
					c.loadModel(this.terrainRender);
				}
			}
		}
		for (int i = -r; i <= r; i++)
		{
			for (int j = -r; j <= r; j++)
			{
				if (!chunks[(i + r) + (j + r) * d])
				{
					Chunk c = new Chunk(i + camx, j + camz, this.terrain.getChunk(i + camx, j + camz), this, this.theTurrem.theRender);
					this.chunks.add(c);
					c.loadModel(this.terrainRender);
				}
			}
		}
	}

	public void doKeyEvent()
	{
		for (Entity e : this.entityList)
		{
			e.keyEvent(this.getEntityPicked() != null ? e.uid == this.getEntityPicked().uid : false);
		}
	}

	public void doMouseEvent()
	{
		for (Entity e : this.entityList)
		{
			e.mouseEvent(this.getEntityPicked() != null ? e.uid == this.getEntityPicked().uid : false);
		}
	}

	public int getHeight(int x, int y)
	{
		Chunk c = this.getChunk(x >> 4, y >> 4);
		if (c != null)
		{
			return c.getHeight(x & 15, y & 15);
		}
		return 0;
	}

	public Chunk getChunk(int cx, int cy)
	{
		for (Chunk c : this.chunks)
		{
			if (c.chunkx == cx && c.chunky == cy)
			{
				return c;
			}
		}
		return null;
	}

	public int unScaleWorld(double x)
	{
		return (int) ((x / 3.2D) * 16.0D);
	}

	public double scaleWorld(int x)
	{
		return (x / 16.0D) * 3.2D;
	}

	public Entity getEntityPicked()
	{
		return this.pickedEntity;
	}

	public void tickWorld()
	{
		this.viewage++;
		this.rendercount += 0.05F;
		this.tickEntities();
		this.pickedEntity = this.calculateEntityPicked();

		if (this.viewage % 10 == 0)
		{
			this.updateChunks(8, 6);
		}

		for (RealmClient realm : this.realms)
		{
			realm.onTick();
		}
	}

	public void tickEntities()
	{
		for (int i = 0; i < this.entityList.size(); i++)
		{
			Entity e = this.entityList.get(i);
			if (e.isDead || !e.isAppear)
			{
				this.entityList.remove(i--);
			}
			else if (!(e instanceof IEntityAmbient))
			{
				e.onTick(10);
			}
		}
	}

	public void render()
	{
		GL11.glPushMatrix();
		for (Chunk c : this.chunks)
		{
			c.renderChunk();
		}
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		Point loc = this.theGame.getFace().getFocus();
		double dist = this.scaleWorld(6 * 16);
		dist = dist * dist;
		for (Entity e : this.entityList)
		{
			double dx = loc.xCoord - e.posX;
			double dz = loc.zCoord - e.posZ;
			if (dx * dx + dz * dz < dist)
			{
				e.render();
			}
		}
		GL11.glPopMatrix();
	}

	public Entity calculateEntityPicked()
	{
		Ray r = this.theGame.getPickRay();
		List<Entity> ents = this.getEntitiesHit(r.getBox());
		Entity returne = null;
		float dist = (float) r.getLengthSqr();
		for (Entity e : ents)
		{
			BoxPin pin = e.getPickBounds().calculateIntercept(r);
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

	public List<Entity> getEntitiesHit(Box box)
	{
		List<Entity> hit = new ArrayList<Entity>();
		for (Entity e : this.entityList)
		{
			if (box.intersectsWith(e.getPickBounds()))
			{
				hit.add(e);
			}
		}
		return hit;
	}

	public List<Entity> getEntitiesHit(Box box, Entity exclude)
	{
		List<Entity> hit = new ArrayList<Entity>();
		long skip = exclude.uid;
		for (Entity e : this.entityList)
		{
			if (e.uid != skip && box.intersectsWith(e.getPickBounds()))
			{
				hit.add(e);
			}
		}
		return hit;
	}

	public final int getViewage()
	{
		return viewage;
	}

	public RenderEngine getTerrainRender()
	{
		return terrainRender;
	}
}
