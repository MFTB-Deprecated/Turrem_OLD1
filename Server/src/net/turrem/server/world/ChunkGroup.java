package net.turrem.server.world;

public class ChunkGroup
{
	public Chunk[] chunks = new Chunk[4096];
	public final int groupx;
	public final int groupy;
	
	public ChunkGroup(int groupx, int groupy)
	{
		this.groupx = groupx;
		this.groupy = groupy;
	}
	
	public Chunk getChunk(int chunkx, int chunky)
	{
		chunkx &= 0x3F;
		chunky &= 0x3F;
		int ind = chunkx + (chunkx << 6);
		Chunk c = this.chunks[ind];
		if (c == null)
		{
			c = this.provide(chunkx, chunky);
			this.chunks[ind] = c;
		}
		return c;
	}
	
	public Chunk provide(int chunkx, int chunky)
	{
		return new Chunk(chunkx, chunky);
	}
}
