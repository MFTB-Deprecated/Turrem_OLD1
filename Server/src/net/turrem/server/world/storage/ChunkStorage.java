package net.turrem.server.world.storage;

import java.util.ArrayList;
import java.util.Collection;

import net.turrem.server.world.Chunk;
import net.turrem.server.world.World;
import net.turrem.server.world.gen.WorldGen;

public class ChunkStorage implements IWorldChunkStorage
{
	public final int width;
	public final int depth;
	public final int mapSize;

	private ChunkQuad[] quads;

	public ChunkStorage(int width, int depth)
	{
		if (depth < 1)
		{
			throw new IllegalArgumentException("Depth must be a positive integer!");
		}
		if (depth > 25)
		{
			throw new IllegalArgumentException("Depth is too large!");
		}
		this.width = width;
		this.depth = depth;
		int s = 1;
		for (int i = 0; i < this.depth; i++)
		{
			s <<= 1;
		}
		this.mapSize = s * this.width;
		this.quads = new ChunkQuad[width * width];
	}

	public Chunk findChunk(int chunkx, int chunkz)
	{
		return this.binaryFindChunk(chunkx, chunkz, false, null);
	}

	public void generateChunk(int chunkx, int chunkz, WorldGen gen)
	{
		this.binaryFindChunk(chunkx, chunkz, true, gen);
	}

	public Chunk findAndGenerateChunk(int chunkx, int chunkz, World world)
	{
		return this.binaryFindChunk(chunkx, chunkz, true, world.theWorldGen);
	}

	private Chunk binaryFindChunk(int chunkx, int chunkz, boolean dogen, WorldGen gen)
	{
		if (!this.isChunkInMap(chunkx, chunkz))
		{
			return null;
		}
		int u = chunkx << (32 - this.depth);
		int v = chunkz << (32 - this.depth);
		int U = chunkx >> (this.depth);
		int V = chunkz >> (this.depth);
		ChunkQuad quad = this.getQuad(U, V, dogen);
		if (quad != null)
		{
			return quad.binaryFindChunk(u, v, dogen, gen);
		}
		return null;
	}

	private ChunkQuad getQuad(int U, int V, boolean make)
	{
		if (U < 0 || U >= this.width || V < 0 || V >= this.width)
		{
			return null;
		}
		int i = U + V * this.width;
		if (this.quads[i] != null)
		{
			return this.quads[i];
		}
		if (make)
		{
			ChunkQuad qu = new ChunkQuad(this, this, this.depth, U, V);
			this.quads[i] = qu;
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
		for (ChunkQuad quad : this.quads)
		{
			if (quad != null)
			{
				quad.getChunks(list);
			}
		}
		return list;
	}

	public Collection<Chunk> getChunks()
	{
		ArrayList<Chunk> list = new ArrayList<Chunk>();
		for (ChunkQuad quad : this.quads)
		{
			if (quad != null)
			{
				quad.getChunks(list);
			}
		}
		return list;
	}

	public Chunk loadChunk(int chunkx, int chunkz)
	{
		return null;
	}

	public Chunk genChunk(int chunkx, int chunkz, WorldGen gen)
	{
		return null;
	}

	public boolean isChunkInMap(int chunkx, int chunkz)
	{
		return chunkx >= 0 && chunkx < this.mapSize && chunkz >= 0 && chunkz < this.mapSize;
	}

	@Override
	public void removeMe(int U, int V)
	{
		int i = U + V * this.width;
		this.quads[i] = null;
	}

	public void worldTick()
	{
		for (ChunkQuad quad : this.quads)
		{
			if (quad != null)
			{
				quad.worldTick();
			}
		}
	}

	public void clear()
	{
		for (int i = 0; i < 4; i++)
		{
			ChunkQuad quad = this.quads[i];
			if (quad != null)
			{
				quad.clear();
			}
			this.quads[i] = null;
		}
	}
}
