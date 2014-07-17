package net.turrem.server.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import net.turrem.server.entity.SoftEntity;

public class EntityStorage
{
	public static class EntityIndexComparator implements Comparator<SoftEntity>
	{
		@Override
		public int compare(SoftEntity o1, SoftEntity o2)
		{
			return Long.compare(o1.entityIdentifier, o2.entityIdentifier);
		}
	}
	
	public World theWorld;
	
	private ArrayList<SoftEntity> entities = new ArrayList<SoftEntity>();
	
	public EntityStorage(World world)
	{
		this.theWorld = world;
	}
	
	public SoftEntity getSoftEntity(int id)
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
	
	public void addEntity(SoftEntity ent)
	{
		if (ent != null)
		{
			boolean sort = false;
			if (!this.entities.isEmpty() && this.entities.get(this.entities.size() - 1).entityIdentifier > ent.entityIdentifier)
			{
				sort = true;
			}
			ent.onPreWorldRegister(this.theWorld);
			this.entities.add(ent);
			ent.onWorldRegister(this.theWorld);
			if (sort)
			{
				this.sortEntities();
			}
		}
	}

	public void worldTick()
	{
		for (int entityIndex = 0; entityIndex < this.entities.size(); entityIndex++)
		{
			SoftEntity entity = this.entities.get(entityIndex);
			if (entity == null || !entity.isAlive())
			{
				this.entities.remove(entityIndex--);
			}
			else
			{
				this.entityTick(entity);
			}
		}
	}
	
	public void entityTick(SoftEntity entity)
	{
		
	}
}
