package zap.turrem.client.game.world;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import zap.turrem.client.game.Game;
import zap.turrem.client.game.RealmClient;
import zap.turrem.client.render.engine.RenderEngine;
import zap.turrem.core.entity.Entity;
import zap.turrem.utils.geo.Box;
import zap.turrem.utils.geo.BoxPin;
import zap.turrem.utils.geo.Point;
import zap.turrem.utils.geo.Ray;

public class WorldClient
{
	public List<Entity> entityList = new ArrayList<Entity>();
	public List<RealmClient> realms = new ArrayList<RealmClient>();

	public Game theGame;

	private Entity pickedEntity = null;
	
	public WorldTerrainGen terrain;
	
	public RenderEngine terrainRender;
	
	public ArrayList<ModelChunk> chunkmodels = new ArrayList<ModelChunk>();

	public WorldClient(Game game)
	{
		this.theGame = game;
		this.terrain = new WorldTerrainGen(15L);
		this.terrain.generate();
		this.terrainRender = new RenderEngine();
		
		for (int i = -8; i < 8; i++)
		{
			for (int j = -8; j < 8; j++)
			{
				this.chunkmodels.add(new ModelChunk(this.terrain.getChunk(i, j), 1.0F, 0.5F, 16, i, j, this.terrainRender));
			}
		}
	}

	public Entity getEntityPicked()
	{
		return this.pickedEntity;
	}

	public void tickWorld()
	{
		this.tickEntities();
		this.pickedEntity = this.calculateEntityPicked();
		
		for (RealmClient realm : this.realms)
		{
			realm.tickTechs();
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
			else
			{
				e.onTick();
			}
		}
	}

	public void render()
	{		
		for (Entity e : this.entityList)
		{
			e.render();
		}		
		
		GL11.glPushMatrix();
		for (ModelChunk c : this.chunkmodels)
		{
			c.render();
		}
		GL11.glPopMatrix();
		
		/**
		GL11.glPushMatrix();
		GL11.glColor3f(0.5F, 0.5F, 0.5F);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glVertex3f(-10.0F, 0.0F, -10.0F);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glVertex3f(-10.0F, 0.0F, 10.0F);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glVertex3f(10.0F, 0.0F, 10.0F);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glVertex3f(10.0F, 0.0F, -10.0F);
		GL11.glEnd();
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
		**/
	}

	public Entity calculateEntityPicked()
	{
		Ray r = this.theGame.getPickRay();
		List<Entity> ents = this.getEntitiesHit(r.getBox());
		Entity returne = null;
		float dist = (float) r.getLengthSqr();
		for (Entity e : ents)
		{
			BoxPin pin = e.getBounds().calculateIntercept(r);
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
			if (box.intersectsWith(e.getBounds()))
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
			if (e.uid != skip && box.intersectsWith(e.getBounds()))
			{
				hit.add(e);
			}
		}
		return hit;
	}
}
