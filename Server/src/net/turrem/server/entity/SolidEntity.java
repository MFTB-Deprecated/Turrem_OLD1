package net.turrem.server.entity;

import net.turrem.server.world.Chunk;

public abstract class SolidEntity implements IEntity
{
	public final int x;
	public final int y;
	public final int z;
	
	public final int entityIdentifier;
	private static int nextId = 0;
	
	private boolean isAlive = true;
	
	public SolidEntity(int x, int y, int z)
	{
		this.entityIdentifier = nextId++;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public int getEntityIdentifier()
	{
		return this.entityIdentifier;
	}
	
	public void onChunkUnload(int chunkX, int chunkZ)
	{
		
	}

	@Override
	public boolean isAlive()
	{
		return isAlive;
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

	public void worldTick(Chunk theChunk)
	{
	}
}
