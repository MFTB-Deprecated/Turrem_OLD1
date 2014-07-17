package net.turrem.server.entity;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.turrem.server.world.storage.WorldStorage;

public abstract class EntityRef
{
	public abstract void write(DataOutput out) throws IOException;
	
	public static EntityRef read(DataInput in) throws IOException
	{
		int id = in.readInt();
		if (id >= 0)
		{
			int entityIdentifier = id;
			return new SoftEntityRef(entityIdentifier);
		}
		else
		{
			int entityIdentifier = (id + 1) * -1;
			int chunkX = in.readInt();
			int chunkZ = in.readInt();
			return new SolidEntityRef(entityIdentifier, chunkX, chunkZ);
		}
	}
	
	public abstract IEntity getEntity(WorldStorage world);
}
