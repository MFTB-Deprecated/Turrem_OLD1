package net.turrem.server.entity;

import net.turrem.server.world.World;

public abstract class SoftEntity implements IEntity
{
	private static int nextId = 0;
	
	public final int entityIdentifier;
	
	public double x;
	public double y;
	public double z;
	
	private boolean isAlive = false;
	
	public SoftEntity()
	{
		this.entityIdentifier = nextId;
		nextId++;
		if (nextId < 0)
		{
			throw new IndexOutOfBoundsException("Entity ID overflow! Over 2,147,483,648 entities have been created!");
		}
	}

	public void onWorldRegister(World world)
	{
		
	}
	
	@Override
	public void kill()
	{
		this.isAlive = false;
		this.onDie();
	}
	
	public void onDie()
	{
		
	}
	
	@Override
	public boolean isAlive()
	{
		return this.isAlive;
	}

	public void onPreWorldRegister(World theWorld)
	{
		this.isAlive = true;
	}
}
