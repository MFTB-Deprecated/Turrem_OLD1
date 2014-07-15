package net.turrem.server.entity;

import net.turrem.server.world.World;

public abstract class SoftEntity implements IEntity
{
	private static long nextId = 0;
	
	public final long entityIdentifier;
	
	public double x;
	public double y;
	public double z;
	
	public SoftEntity()
	{
		this.entityIdentifier = nextId;
		nextId++;
	}

	public void onWorldRegister(World world)
	{
	}
}
