package net.turrem.server.world;

import net.turrem.server.world.storage.ChunkEntityStorage;
import net.turrem.server.world.storage.ChunkQuad;
import net.turrem.server.world.storage.IWorldChunkStorageSegment;

/**
 * A 16x16 region of the world
 */
public class Chunk implements IWorldChunkStorageSegment
{
	public final int chunkx;
	public final int chunkz;
	
	private ChunkQuad parentQuad;
	public ChunkEntityStorage entities;
	
	public Chunk(int chunkx, int chunkz)
	{
		this.chunkx = chunkx;
		this.chunkz = chunkz;
		this.entities = new ChunkEntityStorage(this);
	}
	
	public void unload()
	{
		this.doUnload();
		if (this.parentQuad != null)
		{
			this.parentQuad.removeMe(this.chunkx % 2, this.chunkz % 2);
		}
	}
	
	private void doUnload()
	{
		this.entities.unload();
	}

	public void setParentQuad(ChunkQuad parentQuad)
	{
		this.parentQuad = parentQuad;
	}

	public void worldTick()
	{
		this.entities.worldTick();
	}

	public short getHeight(int x, int z)
	{
		//TODO
		return 0;
	}
}
