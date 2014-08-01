package net.turrem.client.game.world.storage;

import java.util.ArrayList;
import java.util.Collection;

import net.turrem.client.game.world.Chunk;

public class ChunkStorage implements IWorldChunkStorage
{
	public final int width;
	public final int depth;
	public final int mapSize;
	private final int chunksInQuad;

	private ChunkQuad[] quads;

	public static int chunksRendered = 0;

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
		this.chunksInQuad = s;
		this.mapSize = s * this.width;
		this.quads = new ChunkQuad[width * width];
	}

	public Chunk getChunk(int chunkx, int chunkz)
	{
		return this.binaryFindChunk(chunkx, chunkz);
	}

	private Chunk binaryFindChunk(int chunkx, int chunkz)
	{
		if (!this.isChunkInMap(chunkx, chunkz))
		{
			return null;
		}
		int u = chunkx << (32 - this.depth);
		int v = chunkz << (32 - this.depth);
		int U = chunkx >> (this.depth);
		int V = chunkz >> (this.depth);
		ChunkQuad quad = this.getQuad(U, V);
		if (quad != null)
		{
			return quad.binaryFindChunk(u, v);
		}
		return null;
	}

	public Chunk saveChunk(Chunk chunk)
	{
		if (chunk != null)
		{
			return this.binarySetChunk(chunk.chunkx, chunk.chunkz, chunk);
		}
		else
		{
			throw new IllegalArgumentException("Chunk is null! Use \"clearChunk(int, int)\" instead.");
		}
	}

	public Chunk setChunk(int chunkx, int chunkz, Chunk chunk)
	{
		if (chunk != null)
		{
			if (chunk.chunkx != chunkx || chunk.chunkz != chunkz)
			{
				throw new IllegalArgumentException("Chunk coordinates do not match the given coordinates!");
			}
		}
		return this.binarySetChunk(chunkx, chunkz, chunk);
	}

	public Chunk clearChunk(int chunkx, int chunkz)
	{
		return this.binarySetChunk(chunkx, chunkz, null);
	}

	private Chunk binarySetChunk(int chunkx, int chunkz, Chunk chunk)
	{
		if (!this.isChunkInMap(chunkx, chunkz))
		{
			return null;
		}
		int u = chunkx << (32 - this.depth);
		int v = chunkz << (32 - this.depth);
		int U = chunkx >> (this.depth);
		int V = chunkz >> (this.depth);
		if (U < 0 || U >= this.width || V < 0 || V >= this.width)
		{
			int i = U + V * this.width;
			ChunkQuad quad = this.quads[i];
			if (quad == null)
			{
				quad = new ChunkQuad(this, this, this.depth, U, V);
				this.quads[i] = quad;
			}
			return quad.binarySetChunk(u, v, chunk);
		}
		return null;
	}

	private ChunkQuad getQuad(int U, int V)
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
		return null;
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

	public void renderTick(int viewx, int viewz, int chunkRadius, int preciseRadius)
	{
		int chunkx = viewx / 16;
		int chunkz = viewz / 16;

		int xmin = (chunkx % this.chunksInQuad) - chunkRadius;
		int xmax = (chunkx % this.chunksInQuad) + chunkRadius;
		int zmin = (chunkz % this.chunksInQuad) - chunkRadius;
		int zmax = (chunkz % this.chunksInQuad) + chunkRadius;

		int U = chunkx >> (this.depth);
		int V = chunkz >> (this.depth);

		int imin = xmin < 0 ? -1 : 0;
		int jmin = zmin < 0 ? -1 : 0;
		int imax = xmin >= this.chunksInQuad ? 1 : 0;
		int jmax = zmin >= this.chunksInQuad ? 1 : 0;

		int xo;
		int zo;
		for (int i = imin; i <= imax; i++)
		{
			for (int j = jmin; j <= jmax; j++)
			{
				xo = i * this.chunksInQuad;
				zo = j * this.chunksInQuad;
				ChunkQuad quad = this.getQuad(U + i, V + j);
				if (quad != null)
				{
					quad.renderTick(xmin - xo, xmax - xo, zmin - zo, zmax - zo, viewx, viewz, chunkRadius, preciseRadius);
				}
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
