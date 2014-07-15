package net.turrem.server.world;

/**
 * A 16x16 region of the world
 */
public class Chunk implements IWorldChunkStorageSegment
{
	public final int chunkx;
	public final int chunkz;
	
	public Chunk(int chunkx, int chunkz)
	{
		this.chunkx = chunkx;
		this.chunkz = chunkz;
	}
}
