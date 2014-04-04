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
		return this.doGetChunk(chunkx, chunky);
	}

	public Chunk getChunkClamped(int chunkx, int chunky)
	{
		if (chunkx < (groupx * 64) || chunky < (groupy * 64) || chunkx >= (groupx * 64 + 64) || chunky >= (groupy * 64 + 64))
		{
			return null;
		}
		return this.getChunk(chunkx, chunky);
	}

	public void onTick()
	{

	}

	public void unloadAll()
	{
		for (int i = 0; i < this.chunks.length; i++)
		{
			this.unloadChunk(i);
		}
	}

	public void tickUnload()
	{
		for (int i = 0; i < this.chunks.length; i++)
		{
			if (this.chunks[i] != null)
			{
				if (this.chunks[i].tickUnload(2))
				{
					this.unloadChunk(i);
				}
			}
		}
	}

	private void unloadChunk(int k)
	{

	}

	public Chunk doGetChunk(int i, int j)
	{
		int ind = i + (j << 6);
		Chunk c = this.chunks[ind];
		if (c == null)
		{
			c = this.provide(i, j);
			this.chunks[ind] = c;
		}
		return c;
	}

	protected Chunk provide(int chunkx, int chunky)
	{
		return new Chunk(chunkx, chunky);
	}
}
