package net.turrem.server.world.mesh;

import java.util.HashMap;

public class WorldMesh
{
	private HashMap<Integer, HashMap<Integer, WorldVertex>> map;
	public int seed;
	
	public WorldMesh(int seed)
	{
		this.map = new HashMap<Integer, HashMap<Integer, WorldVertex>>();
		this.seed = seed;
	}
	
	public WorldVertex getVertex(int row, int col)
	{
		HashMap<Integer, WorldVertex> r = this.map.get(row);
		if (r == null)
		{
			return null;
		}
		return r.get(col);
	}
	
	public WorldVertex createVertex(int row, int col)
	{
		HashMap<Integer, WorldVertex> r = this.map.get(row);
		if (r == null)
		{
			r = new HashMap<Integer, WorldVertex>();
			this.map.put(row, r);
		}
		WorldVertex v = r.get(col);
		if (v == null)
		{
			v = new WorldVertex(this, row, col, this.posSeed(row, col));
			r.put(col, v);
		}
		return v;
	}
	
	private long posSeed(long x, long y)
	{
		long seed = this.seed;
		seed *= seed * 6364136223846793005L + 1442695040888963407L;
		seed += x;
		seed *= seed * 6364136223846793005L + 1442695040888963407L;
		seed += y;
		seed *= seed * 6364136223846793005L + 1442695040888963407L;
		seed += x;
		seed *= seed * 6364136223846793005L + 1442695040888963407L;
		seed += y;
		return seed;
	}
}
