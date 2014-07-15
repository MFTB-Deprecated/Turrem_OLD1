package net.turrem.server.world;

import java.util.Collection;

import net.turrem.server.world.gen.WorldGen;

public class ChunkQuad implements IWorldChunkStorageSegment, IWorldChunkStorage
{
	public ChunkStorage theStorage;
	public IWorldChunkStorage theParent;
	public final int thisDepth;
	public final int scale;
	public final int xpos;
	public final int zpos;

	private IWorldChunkStorageSegment[] quad = new IWorldChunkStorageSegment[4];

	public ChunkQuad(ChunkStorage storage, IWorldChunkStorage parent, int depth, int x, int z) throws IllegalArgumentException
	{
		this.theStorage = storage;
		this.theParent = parent;
		this.xpos = x;
		this.zpos = z;
		if (depth < 1)
		{
			throw new IllegalArgumentException("Depth must be a positive integer!");
		}
		if (depth > 25)
		{
			throw new IllegalArgumentException("Depth is too large!");
		}
		this.thisDepth = depth;
		int s = 1;
		for (int i = 1; i < this.thisDepth; i++)
		{
			s <<= 1;
		}
		this.scale = s;
	}

	public Chunk binaryFindChunk(int u, int v, boolean dogen, WorldGen gen)
	{
		int i = 0;
		if (u < 0)
		{
			i |= 1;
		}
		if (v < 0)
		{
			i |= 2;
		}
		if (this.thisDepth == 1)
		{
			return this.getChunk(i, dogen, gen);
		}
		else
		{
			ChunkQuad quad = this.getQuad(i, dogen);
			if (quad == null)
			{
				return null;
			}
			return quad.binaryFindChunk(u << 1, v << 1, dogen, gen);
		}
	}

	private Chunk getChunk(int i, boolean dogen, WorldGen gen)
	{
		if (this.quad[i] != null)
		{
			return (Chunk) this.quad[i];
		}
		int u = (i >> 0) & 1;
		int v = (i >> 1) & 1;
		int chunkx = this.xpos * 2 + u;
		int chunkz = this.zpos * 2 + v;
		Chunk chunk = this.theStorage.loadChunk(chunkx, chunkz);
		if (chunk != null)
		{
			this.quad[i] = chunk;
			return chunk;
		}
		if (dogen)
		{
			chunk = this.theStorage.genChunk(chunkx, chunkz, gen);
			if (chunk != null)
			{
				this.quad[i] = chunk;
				return chunk;
			}
		}
		return null;
	}

	private ChunkQuad getQuad(int i, boolean make)
	{
		int u = (i >> 0) & 1;
		int v = (i >> 1) & 1;
		if (this.quad[i] != null)
		{
			return (ChunkQuad) this.quad[i];
		}
		if (make)
		{
			ChunkQuad qu = new ChunkQuad(this.theStorage, this, this.thisDepth - 1, this.xpos * 2 + u, this.xpos * 2 + v);
			this.quad[i] = qu;
			return qu;
		}
		else
		{
			return null;
		}
	}

	@Override
	public Collection<Chunk> getChunks(Collection<Chunk> list)
	{
		if (this.thisDepth == 1)
		{
			for (IWorldChunkStorageSegment seg : this.quad)
			{
				if (seg != null && seg instanceof Chunk)
				{
					list.add((Chunk) seg);
				}
			}
		}
		else
		{
			for (IWorldChunkStorageSegment seg : this.quad)
			{
				if (seg != null && seg instanceof IWorldChunkStorage)
				{
					((IWorldChunkStorage) seg).getChunks(list);
				}
			}
		}
		return list;
	}
}
