package net.turrem.server.world;

import java.util.ArrayList;
import java.util.List;

import net.turrem.server.world.material.Material;
import net.turrem.server.world.storage.ChunkEntityStorage;
import net.turrem.server.world.storage.ChunkQuad;
import net.turrem.server.world.storage.IWorldChunkStorageSegment;

/**
 * A 16x16 region of the world
 */
public class Chunk implements IWorldChunkStorageSegment
{
	public final int chunkx;
	public final int chunkz;
	
	private ChunkQuad parentQuad;
	public ChunkEntityStorage entities;
	
	private short[] baseRock = new short[256];
	private short[] heightMap = new short[256];
	private boolean needsRebuild = true;
	private ArrayList<Stratum> strata = new ArrayList<Stratum>();
	private short minHeight = Short.MAX_VALUE;
	private short maxHeight = Short.MIN_VALUE;
	
	public Chunk(int chunkx, int chunkz)
	{
		this.chunkx = chunkx;
		this.chunkz = chunkz;
		this.entities = new ChunkEntityStorage(this);
	}
	
	public void unload()
	{
		this.doUnload();
		if (this.parentQuad != null)
		{
			this.parentQuad.removeMe(this.chunkx % 2, this.chunkz % 2);
		}
	}
	
	private void doUnload()
	{
		this.entities.unload();
	}
	
	public void setParentQuad(ChunkQuad parentQuad)
	{
		this.parentQuad = parentQuad;
	}
	
	public void worldTick()
	{
		this.entities.worldTick();
	}
	
	public short getHeight(int x, int z)
	{
		this.build();
		return this.heightMap[(x % 16) + (z % 16) * 16];
	}
	
	public short[] getHeight()
	{
		this.build();
		return this.heightMap;
	}
	
	protected void build()
	{
		if (this.needsRebuild)
		{
			this.needsRebuild = false;
			this.doRebuild();
		}
	}
	
	protected void doRebuild()
	{
		short[] height = this.baseRock.clone();
		for (int i = 0; i < this.strata.size(); i++)
		{
			Stratum st = this.strata.get(i);
			if (st.getVolume() == 0)
			{
				this.strata.remove(i--);
			}
			else
			{
				st.sumHeight(height);
			}
		}
		this.heightMap = height;
		this.minHeight = Short.MAX_VALUE;
		this.maxHeight = Short.MIN_VALUE;
		for (int i = 0; i < 256; i++)
		{
			short h = this.heightMap[i];
			if (h < this.minHeight)
			{
				this.minHeight = h;
			}
			if (h > this.maxHeight)
			{
				this.maxHeight = h;
			}
		}
	}
	
	public void addUnder(List<Stratum> newstrata)
	{
		for (Stratum st : newstrata)
		{
			st.subHeight(this.baseRock);
		}
		this.strata.addAll(0, newstrata);
		this.doRebuild();
	}
	
	public void generateWithTop(short[] surfaceHeight, List<Stratum> genstrata)
	{
		this.strata.clear();
		this.baseRock = surfaceHeight.clone();
		for (Stratum st : genstrata)
		{
			st.subHeight(this.baseRock);
		}
		this.strata.addAll(genstrata);
		this.doRebuild();
	}
	
	public void generateWithBase(short[] baseRockHeight, List<Stratum> genstrata)
	{
		this.strata.clear();
		this.baseRock = baseRockHeight.clone();
		this.strata.addAll(genstrata);
		this.doRebuild();
	}
	
	public short getMinHeight()
	{
		this.build();
		return this.minHeight;
	}
	
	public short getMaxHeight()
	{
		this.build();
		return this.maxHeight;
	}
	
	public Material[] coreTerrain(int x, int size, int z)
	{
		x %= 16;
		z %= 16;
		Material[] core = new Material[size];
		int pos = 0;
		for (int i = this.strata.size() - 1; i >= 0; i--)
		{
			Stratum st = this.strata.get(i);
			short depth = st.getDepth(x, z);
			for (int j = 0; j < depth && pos < size; j++)
			{
				core[pos] = st.material;
				pos++;
			}
			if (pos >= size)
			{
				break;
			}
		}
		return core;
	}
	
	public Stratum getTopStratum(int x, int z)
	{
		for (int i = this.strata.size() - 1; i >= 0; i--)
		{
			Stratum st = this.strata.get(i);
			short depth = st.getDepth(x % 16, z % 16);
			if (depth > 0)
			{
				return st;
			}
		}
		return null;
	}
}
