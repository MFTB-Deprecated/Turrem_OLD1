package net.turrem.server.world.storage;

import java.util.ArrayList;
import java.util.Collections;

import net.turrem.server.entity.SolidEntity;
import net.turrem.server.world.Chunk;
import net.turrem.server.world.EntityIndexComparator;

public class ChunkEntityStorage
{
	protected ArrayList<SolidEntity> entities = new ArrayList<SolidEntity>();
	public Chunk theChunk;
	
	public ChunkEntityStorage(Chunk chunk)
	{
		this.theChunk = chunk;
	}
	
	public SolidEntity getSolidEntity(int id)
	{
		int num = this.entities.size();
		if (num == 0)
		{
			return null;
		}
		int size = num;
		int exp = 0;
		while (size != 1)
		{
			size >>>= 1;
			exp++;
		}
		size = 1;
		size <<= exp;
		if (num > size)
		{
			size <<= 1;
		}
		size >>>= 1;
		int select = size;
		int its = 0;
		while (its++ <= exp + 1)
		{
			if (size > 1)
			{
				size >>>= 1;
			}
			if (select >= num)
			{
				select -= size;
			}
			else
			{
				int sid = this.entities.get(select).entityIdentifier;
				if (sid == id)
				{
					return this.entities.get(select);
				}
				if (sid > id)
				{
					select -= size;
				}
				if (sid < id)
				{
					select += size;
				}
			}
		}
		return null;
	}
	
	public void sortEntities()
	{
		Collections.sort(this.entities, new EntityIndexComparator());
	}
	
	public void unload()
	{
		for (SolidEntity ent : this.entities)
		{
			ent.onChunkUnload(this.theChunk.chunkx, this.theChunk.chunkz);
		}
		this.entities.clear();
	}

	public void worldTick()
	{
		for (int i = 0; i < this.entities.size(); i++)
		{
			SolidEntity entity = this.entities.get(i);
			
			if (entity == null)
			{
				this.entities.remove(i--);
			}
			else
			{
				entity.worldTick(this.theChunk);
				if (!entity.isAlive())
				{
					this.entities.remove(i--);
				}
			}
		}
	}
}
