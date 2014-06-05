package net.turrem.client.game.world;

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
		int i = chunkx & 0x3F;
		int j = chunky & 0x3F;
		int k = i + (j << 6);
		Chunk c = this.chunks[k];
		return c;
	}
	
	public void renderChunks(int mincx, int mincz, int maxcx, int maxcz, int px, int pz, ClientWorld world)
	{
		mincx -= this.groupx * 64;
		mincz -= this.groupy * 64;
		maxcx -= this.groupx * 64;
		maxcz -= this.groupy * 64;
		mincx = mincx < 0 ? 0 : mincx;
		mincx = mincx >= 64 ? 64 : mincx;
		mincz = mincz < 0 ? 0 : mincz;
		mincz = mincz >= 64 ? 64 : mincz;
		maxcx = maxcx < 0 ? 0 : maxcx;
		maxcx = maxcx >= 64 ? 64 : maxcx;
		maxcz = maxcz < 0 ? 0 : maxcz;
		maxcz = maxcz >= 64 ? 64 : maxcz;
		for (int i = mincx; i < maxcx; i++)
		{
			for (int j = mincz; j < maxcz; j++)
			{
				int k = i + (j << 6);
				Chunk c = this.chunks[k];
				if (c != null)
				{
					world.renderChunk(c, px, pz);
				}
			}
		}
	}
	
	public void setChunk(int chunkx, int chunky, Chunk chunk)
	{
		int i = chunkx & 0x3F;
		int j = chunky & 0x3F;
		int k = i + (j << 6);
		Chunk old = this.chunks[k];
		if (old != null)
		{
			old.unload();
		}
		this.chunks[k] = chunk;
	}

	public void unload()
	{
		for (Chunk c : this.chunks)
		{
			if (c != null)
			{
				c.unload();
			}
		}
	}
}
