package net.turrem.client.game.world.storage;

import java.util.Collection;

import net.turrem.client.game.world.Chunk;

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
		for (int i = 0; i < this.thisDepth; i++)
		{
			s <<= 1;
		}
		this.scale = s;
	}

	public Chunk binaryFindChunk(int u, int v)
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
			return (Chunk) this.quad[i];
		}
		else
		{
			ChunkQuad quad = (ChunkQuad) this.quad[i];
			if (quad == null)
			{
				return null;
			}
			return quad.binaryFindChunk(u << 1, v << 1);
		}
	}

	public Chunk binarySetChunk(int u, int v, Chunk chunk)
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
			Chunk old = (Chunk) this.quad[i];
			this.quad[i] = chunk;
			if (chunk == null)
			{
				this.checkRemove();
			}
			return old;
		}
		else
		{
			ChunkQuad quad = (ChunkQuad) this.quad[i];
			if (quad == null)
			{
				quad = new ChunkQuad(this.theStorage, this, this.thisDepth - 1, this.xpos * 2 + (i % 2), this.zpos * 2 + (i / 2));
				this.quad[i] = quad;
			}
			return quad.binarySetChunk(u << 1, v << 1, chunk);
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

	@Override
	public void removeMe(int U, int V)
	{
		int i = U + V * 2;
		this.quad[i] = null;
		this.checkRemove();
	}

	public boolean checkRemove()
	{
		for (int j = 0; j < 4; j++)
		{
			if (this.quad[j] != null)
			{
				return false;
			}
		}
		this.theParent.removeMe(this.xpos % 2, this.zpos % 2);
		return true;
	}

	public void clear()
	{
		for (int i = 0; i < 4; i++)
		{
			IWorldChunkStorageSegment seg = this.quad[i];
			if (seg != null && seg instanceof ChunkQuad)
			{
				((ChunkQuad) seg).clear();
			}
			else if (seg != null && seg instanceof Chunk)
			{
				((Chunk) seg).unload();
			}
			this.quad[i] = null;
		}
	}

	public void renderTick(int xmin, int xmax, int zmin, int zmax, int viewx, int viewz, int chunkRadius, int preciseRadius)
	{
		xmin = this.clip(xmin);
		xmax = this.clip(xmax);
		zmin = this.clip(zmin);
		zmax = this.clip(zmax);
		if (this.thisDepth == 1)
		{
			for (int i = xmin; i <= xmax; i++)
			{
				for (int j = zmin; j <= zmax; j++)
				{
					Chunk c = ((Chunk) this.quad[i + j * 2]);
					if (c != null)
					{
						c.render(viewx, viewz, chunkRadius, preciseRadius);
					}
				}
			}
		}
		else
		{
			this.renderTickQuad(xmin, xmax, zmin, zmax, viewx, viewz, chunkRadius, preciseRadius);
		}
	}

	private void renderTickQuad(int xmin, int xmax, int zmin, int zmax, int viewx, int viewz, int radius, int preciseRadius)
	{
		int xo;
		int zo;
		for (int i = this.getSide(xmin); i <= this.getSide(xmax); i++)
		{
			for (int j = this.getSide(zmin); j <= this.getSide(zmax); j++)
			{
				ChunkQuad quad = ((ChunkQuad) this.quad[i + j * 2]);
				if (quad != null)
				{
					xo = i * (this.scale / 2);
					zo = j * (this.scale / 2);
					quad.renderTick(xmin - xo, xmax - xo, zmin - zo, zmax - zo, viewx, viewz, radius, preciseRadius);
				}
			}
		}
	}

	private int clip(int x)
	{
		if (x < 0)
		{
			return 0;
		}
		if (x >= this.scale)
		{
			return this.scale - 1;
		}
		return x;
	}

	private int getSide(int x)
	{
		if (x >= this.scale / 2)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
}
