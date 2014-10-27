package net.turrem.app.server.entity;

import java.io.DataOutput;
import java.io.IOException;

import net.turrem.app.server.world.storage.WorldStorage;

public class SoftEntityRef extends EntityRef
{
	public int entityIdentifier;
	
	public SoftEntityRef(int entityIdentifier)
	{
		this.entityIdentifier = entityIdentifier;
	}
	
	@Override
	public void write(DataOutput out) throws IOException
	{
		out.writeInt(this.entityIdentifier);
	}
	
	@Override
	public IEntity getEntity(WorldStorage world)
	{
		return world.entities.getSoftEntity(this.entityIdentifier);
	}
	
	public SoftEntity getSoftEntity(WorldStorage world)
	{
		return world.entities.getSoftEntity(this.entityIdentifier);
	}
}
