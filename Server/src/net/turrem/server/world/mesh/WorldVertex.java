package net.turrem.server.world.mesh;

import java.util.Random;

import net.turrem.server.world.morph.IGeomorph;

public class WorldVertex
{
	public static enum EnumMeshNeighbor
	{
		TOP_LEFT(-1, -1),
		TOP_RIGHT(-1, 0),
		LEFT(0, -1),
		RIGHT(0, 1),
		BOTTOM_LEFT(1, 0),
		BOTTOM_RIGHT(1, 1);
		
		public final int row;
		public final int col;
		
		EnumMeshNeighbor(int row, int col)
		{
			this.row = row;
			this.col = col;
		}
	}
	
	public static final int maxLevel = 2;
	
	protected final WorldMesh mesh;
	public final int row;
	public final int col;
	
	private WorldVertex[] neighbors = new WorldVertex[6];
	
	private VertexGenData data;
	private VertexGenData lastData;
	
	private int level;
	
	protected long seed;
	
	public WorldVertex(WorldMesh mesh, int row, int col, long seed, VertexGenData start)
	{
		this.mesh = mesh;
		this.row = row;
		this.col = col;
		this.seed = seed;
		this.level = 0;
		this.lastData = null;
		this.data = this.generate(start);
	}
	
	public WorldVertex getNeighbor(EnumMeshNeighbor neighbor)
	{
		return this.getNeighbor(neighbor.ordinal());
	}
	
	public WorldVertex getNeighbor(int neighbor)
	{
		return this.neighbors[neighbor];
	}
	
	public int getLevel()
	{
		return this.level;
	}
	
	void upgrade()
	{
		if (this.level == maxLevel)
		{
			return;
		}
		for (int i = 0; i < 6; i++)
		{
			EnumMeshNeighbor n = EnumMeshNeighbor.values()[i];
			int r = this.row + n.row;
			int c = this.col + n.col;
			if (this.neighbors[i] == null)
			{
				this.neighbors[i] = this.mesh.createVertex(r, c);
			}
			if (this.neighbors[i].getLevel() < this.getLevel())
			{
				this.neighbors[i].upgrade();
			}
		}
		this.level++;
		
		this.lastData = this.data;
		this.data = this.generate(this.data);
	}
	
	VertexGenData getData()
	{
		return this.data;
	}
	
	public VertexGenData getVisibleGenData(int atLevel)
	{
		if (this.getLevel() < atLevel)
		{
			return this.data;
		}
		if (this.getLevel() == atLevel)
		{
			return this.lastData;
		}
		return null;
	}
	
	public VertexGenData getNeighborGenData(int neighbor)
	{
		if (this.neighbors[neighbor] != null)
		{
			return this.neighbors[neighbor].getVisibleGenData(this.getLevel());
		}
		return null;
	}
	
	public VertexGenData getNeighborGenData(EnumMeshNeighbor neighbor)
	{
		return this.getNeighborGenData(neighbor.ordinal());
	}
	
	protected VertexGenData generate(VertexGenData last)
	{
		VertexGenDataWork work;
		if (last == null)
		{
			work = new VertexGenDataWork();
		}
		else
		{
			work = new VertexGenDataWork(last.height);
		}
		if (last == null)
		{
			/*
			 * for (IGeomorph morph : ) {
			 * 
			 * }
			 */
		}
		else
		{
			for (IGeomorph morph : last.morphs)
			{
				morph.generateUpgrade(work, last, this, new Random(morph.getSeed(this.seed)));
			}
		}
		return new VertexGenData(work);
	}
}
