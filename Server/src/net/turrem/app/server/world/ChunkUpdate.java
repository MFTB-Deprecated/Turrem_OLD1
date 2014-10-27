package net.turrem.app.server.world;

public class ChunkUpdate
{
	public final int chunkx;
	public final int chunkz;
	
	public ChunkUpdate(int chunkx, int chunkz)
	{
		this.chunkx = chunkx;
		this.chunkz = chunkz;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ChunkUpdate)
		{
			ChunkUpdate cu = (ChunkUpdate) obj;
			return cu.chunkx == this.chunkx && cu.chunkz == this.chunkz;
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		int hash = 1;
		hash = hash * 17 + ((Integer) this.chunkx).hashCode();
		hash = hash * 31 + ((Integer) this.chunkz).hashCode();
		return hash;
	}
}
