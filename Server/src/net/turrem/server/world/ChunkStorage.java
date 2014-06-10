package net.turrem.server.world;

import net.turrem.server.Config;
import net.turrem.server.Realm;

public class ChunkStorage
{
	protected World theWorld;
	private ChunkGroup[] array;
	public final int width;

	public ChunkStorage(int width, World world)
	{
		this.theWorld = world;
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
		i += this.width / 2;
		j += this.width / 2;
		if (i < 0 || i >= this.width - 1 || j < 0 || j >= this.width - 1)
		{
			return null;
		}
		aro[0] = this.getGroup(i, j);
		aro[1] = this.getGroup(i + 1, j);
		aro[2] = this.getGroup(i, j + 1);
		aro[3] = this.getGroup(i + 1, j + 1);
		return aro;
	}

	private boolean isChunkOutside(int chunkx, int chunkz)
	{
		int i = chunkx;
		int j = chunkz;
		i >>= 6;
		j >>= 6;
		i += this.width / 2;
		j += this.width / 2;
		return (i < 0 || i >= this.width - 1 || j < 0 || j >= this.width - 1);
	}
	
	public void sendChunkUpdateToClients(int chunkx, int chunkz)
	{
		int vis = this.getVisibility(chunkx, chunkz);
		ChunkUpdate update = this.getChunkUpdate(chunkx, chunkz);
		
		for (int i = 0; i < 16; i++)
		{
			if ((vis & 1) == 1)
			{
				Realm realm = this.theWorld.getRealms()[i];
				if (realm != null)
				{
					realm.addChunkUpdate(update);
				}
			}
			vis >>>= 1;
		}
	}
	
	public ChunkUpdate getChunkUpdate(int chunkx, int chunkz)
	{
		return new ChunkUpdate(chunkx, chunkz);
	}

	private ChunkGroup getGroup(int i, int j)
	{
		int k = i + j * this.width;
		if (this.array[k] == null)
		{
			this.array[k] = new ChunkGroup(i - this.width / 2, j - this.width / 2, this.theWorld);
		}
		return this.array[k];
	}

	public void tick(long time)
	{
		for (int i = 0; i < this.array.length; i++)
		{
			if (this.array[i] != null)
			{
				this.array[i].onTick();
				if ((i + time) % Config.chunkUnloadTickMod == 0)
				{
					this.array[i].tickUnload();
				}
			}
		}
	}
	
	public void processVisibility()
	{
		for (int i = 0; i < this.array.length; i++)
		{
			if (this.array[i] != null)
			{
				this.array[i].processVisibility();
			}
		}
	}

	public void setVisibility(int chunkx, int chunkz, int realm, boolean visible)
	{
		if (!this.isChunkOutside(chunkx, chunkz))
		{
			int i = this.getIndex(chunkx, chunkz);
			ChunkGroup cg = this.array[i];
			if (cg == null)
			{
				if (visible)
				{
					cg = new ChunkGroup(chunkx >> 6, chunkz >> 6, this.theWorld);
					this.array[i] = cg;
				}
				else
				{
					return;
				}
			}
			cg.setVisibility(chunkx, chunkz, realm, visible);
		}
	}

	public boolean getVisibility(int chunkx, int chunkz, int realm)
	{
		if (this.isChunkOutside(chunkx, chunkz))
		{
			return false;
		}
		int i = this.getIndex(chunkx, chunkz);
		ChunkGroup cg = this.array[i];
		if (cg == null)
		{
			return false;
		}
		return cg.getVisibility(chunkx, chunkz, realm);
	}

	public int getVisibility(int chunkx, int chunkz)
	{
		if (this.isChunkOutside(chunkx, chunkz))
		{
			return 0;
		}
		int i = this.getIndex(chunkx, chunkz);
		ChunkGroup cg = this.array[i];
		if (cg == null)
		{
			return 0;
		}
		return cg.getVisibility(chunkx, chunkz);
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
		if (this.isChunkOutside(chunkx, chunkz))
		{
			return null;
		}
		int i = this.getIndex(chunkx, chunkz);
		ChunkGroup cg = this.array[i];
		if (cg == null)
		{
			cg = new ChunkGroup(chunkx >> 6, chunkz >> 6, this.theWorld);
			this.array[i] = cg;
		}
		Chunk ch = cg.getChunk(chunkx, chunkz);
		return ch;
	}

	public ChunkGroup[] getArray()
	{
		return array;
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
				this.array[i].unloadAll();
				this.array[i] = null;
			}
		}
	}
}
