package net.turrem.client.game.world;

public class ChunkStorage
{
	private Chunk[] array;
	public final int width;
	
	public ChunkStorage(int width)
	{
		this.width = width;
		this.array = new Chunk[width * width];
	}
	
	private int getIndex(int i, int j)
	{
		i = this.mod(i);
		j = this.mod(j);
		return i + j * this.width;
	}
	
	private int mod(int value)
	{
		return (value % this.width + this.width) % this.width;
	}
	
	public Chunk getChunk(int chunkx, int chunkz)
	{
		int i = this.getIndex(chunkx, chunkz);
		Chunk ch = this.array[i];
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
	
	public Chunk[] getArray()
	{
		return this.array;
	}
	
	public void setChunk(Chunk chunk)
	{
		if (chunk != null)
		{
			int i = this.getIndex(chunk.chunkx, chunk.chunkz);
			if (this.array[i] != null)
			{
				this.array[i].unload();
			}
			this.array[i] = chunk;
		}
	}
	
	public boolean setChunk(Chunk chunk, int closex, int closez)
	{
		if (chunk != null)
		{
			int i = this.getIndex(chunk.chunkx, chunk.chunkz);
			Chunk old = this.array[i];
			if (old != null)
			{
				if (old.chunkx == chunk.chunkx && old.chunkz == chunk.chunkz)
				{
					this.array[i].unload();
					this.array[i] = chunk;
					return true;
				}
				else if (this.distSqr(old.chunkx, closex, old.chunkz, closez) >= this.distSqr(chunk.chunkx, closex, chunk.chunkz, closez))
				{
					this.array[i].unload();
					this.array[i] = chunk;
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				this.array[i] = chunk;
				return true;
			}
		}
		return false;
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
