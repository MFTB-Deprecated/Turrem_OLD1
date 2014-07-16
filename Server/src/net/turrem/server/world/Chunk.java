package net.turrem.server.world;

/**
 * A 16x16 region of the world
 */
public class Chunk implements IWorldChunkStorageSegment
{
	public final int chunkx;
	public final int chunkz;
	
	private ChunkQuad parentQuad;
	
	public Chunk(int chunkx, int chunkz)
	{
		this.chunkx = chunkx;
		this.chunkz = chunkz;
	}
	
	public void unload()
	{
		if (this.parentQuad != null)
		{
			this.parentQuad.removeMe(this.chunkx % 2, this.chunkz % 2);
		}
		this.doUnload();
	}
	
	private void doUnload()
	{
		
	}

	public void setParentQuad(ChunkQuad parentQuad)
	{
		this.parentQuad = parentQuad;
	}
}
