package net.turrem.server.entity;

import net.turrem.server.world.Chunk;

public abstract class SolidEntity implements IEntity
{
	public int x;
	public int y;
	public int z;
	
	public final int entityIdentifier;
	private static int nextId = 0;
	
	private boolean isAlive = true;
	
	public SolidEntity()
	{
		this.entityIdentifier = nextId++;
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
