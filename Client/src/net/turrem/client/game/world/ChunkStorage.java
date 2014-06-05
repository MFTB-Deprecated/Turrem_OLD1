package net.turrem.client.game.world;

public class ChunkStorage
{
	private ChunkGroup[] array;
	public final int width;
	
	public ChunkStorage(int width)
	{
		this.width = width;
		this.array = new ChunkGroup[width * width];
	}
	
	public ChunkGroup[] getChunksAround(int chunkx, int chunkz)
	{
		ChunkGroup[] aro = new ChunkGroup[4];
		int i = chunkx - 32;
		int j = chunkz - 32;
		i >>= 6;
		j >>= 6;
		i += width / 2;
		j += width / 2;
		int k = i + j * this.width;
		aro[0] = this.array[k];
		aro[1] = this.array[k + 1];
		aro[2] = this.array[k + this.width];
		aro[3] = this.array[k + this.width + 1];
		return aro;
	}
	
	private int getIndex(int i, int j)
	{
		i >>= 6;
		j >>= 6;
		i += width / 2;
		j += width / 2;
		return i + j * this.width;
	}
	
	public Chunk getChunk(int chunkx, int chunkz)
	{
		int i = this.getIndex(chunkx, chunkz);
		ChunkGroup cg = this.array[i];
		if (cg == null)
		{
			return null;
		}
		Chunk ch = cg.getChunk(chunkx, chunkz);
		if (ch == null)
		{
			return null;
		}
		if (ch.chunkx == chunkx && ch.chunkz == chunkz)
		{
			return ch;
		}
		return null;
	}
	
	public ChunkGroup[] getArray()
	{
		return array;
	}

	public void setChunk(Chunk chunk)
	{
		if (chunk != null)
		{
			int i = this.getIndex(chunk.chunkx, chunk.chunkz);
			ChunkGroup cg = this.array[i];
			if (cg == null)
			{
				cg = new ChunkGroup(chunk.chunkx >> 6, chunk.chunkz >> 6);
				this.array[i] = cg;
			}
			cg.setChunk(chunk.chunkx, chunk.chunkz, chunk);
		}
	}
	
	public int distSqr(int dx, int dz)
	{
		return dx * dx + dz * dz;
	}
	
	public int distSqr(int x0, int x1, int z0, int z1)
	{
		return this.distSqr(x0 - x1, z0 - z1);
	}
	
	public void clear()
	{
		for (int i = 0; i < this.array.length; i++)
		{
			if (this.array[i] != null)
			{
				this.array[i].unload();
				this.array[i] = null;
			}
		}
	}
}
