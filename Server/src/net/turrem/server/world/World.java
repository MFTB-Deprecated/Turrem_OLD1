package net.turrem.server.world;

import java.util.ArrayList;
import java.util.HashMap;

import net.turrem.server.entity.Entity;

public class World
{
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public HashMap<Integer, ChunkGroup> chunks = new HashMap<Integer, ChunkGroup>();
	public long worldTime = 0;

	public void addEntity(Entity ent)
	{
		if (ent != null)
		{
			this.entities.add(ent);
		}
	}

	public void tick()
	{
		this.worldTime++;
		this.updateEntities();
	}

	public void updateEntities()
	{
		for (int i = 0; i < this.entities.size(); i++)
		{
			Entity ent = this.entities.get(i);

			int x;
			int z;
			int cx;
			int cz;
			int cxmin;
			int czmin;
			int cxmax;
			int czmax;

			if (ent.isDead())
			{
				this.entities.remove(i--);
			}
			else
			{
				ent.onTick();
				if ((this.worldTime + i) % 10 == 0)
				{
					int r = ent.loadRadius();
					if (r > 1)
					{
						x = (int) ent.x;
						z = (int) ent.z;
						ChunkGroup g = this.retriveGroup(x, z);
						cx = (x >> 4) & 0x3F;
						cz = (z >> 4) & 0x3F;
						cxmin = cx - r;
						if (cxmin < 0)
						{
							cxmin = 0;
						}
						czmin = cz - r;
						if (czmin < 0)
						{
							czmin = 0;
						}
						cxmax = cx + r;
						if (cxmax > 63)
						{
							cxmax = 63;
						}
						czmax = cz + r;
						if (czmax > 63)
						{
							czmax = 63;
						}
						for (int ci = cxmin; ci < cxmax; ci++)
						{
							for (int cj = czmin; cj < czmax; cj++)
							{
								Chunk c = g.doGetChunk(ci, cj);
								if (c != null)
								{
									c.onEntityTick();
								}
							}
						}
					}
					else if (r == 1)
					{
						x = (int) ent.x;
						z = (int) ent.z;
						Chunk c = this.getChunk(x, z);
						if (c != null)
						{
							c.onEntityTick();
						}
					}
				}
			}
		}
	}

	public ChunkGroup getGroup(int x, int y)
	{
		int i = x >> 10;
		int j = y >> 10;
		int k = i | (j << 16);
		return this.chunks.get(k);
	}

	public ChunkGroup retriveGroup(int x, int y)
	{
		int i = x >> 10;
		int j = y >> 10;
		int k = i | (j << 16);
		ChunkGroup c = this.chunks.get(k);
		if (c == null)
		{
			c = new ChunkGroup(i, j);
			this.chunks.put(k, c);
		}
		return c;
	}

	public Chunk getChunk(int x, int y)
	{
		ChunkGroup g = this.retriveGroup(x, y);
		if (g != null)
		{
			return g.getChunk(x >> 4, y >> 4);
		}
		return null;
	}
}
