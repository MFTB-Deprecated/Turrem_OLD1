package net.turrem.server.world.mesh;

import java.util.HashMap;

public class WorldMesh
{
	private HashMap<Integer, HashMap<Integer, WorldVertex>> map;
	public int seed;
	public WorldMesh parent;
	
	public WorldMesh(int seed, WorldMesh parent)
	{
		this.map = new HashMap<Integer, HashMap<Integer, WorldVertex>>();
		this.seed = seed;
		this.parent = parent;
	}
	
	/**
	 * Gets the world vertex at the given row and column.
	 * 
	 * @param row The row that the vertex lies on.
	 * @param col The diagonal line that the vertex lies on, see
	 *            {@link WorldVertex.EnumMeshNeighbor#col}.
	 * @return The vertex at the given coordinate, or null if that vertex has
	 *         not been created.
	 */
	public WorldVertex getVertex(int row, int col)
	{
		HashMap<Integer, WorldVertex> r = this.map.get(row);
		if (r == null)
		{
			return null;
		}
		return r.get(col);
	}
	
	/**
	 * Gets the world vertex at the given row and column. If the vertex has not
	 * been generated yet, it is created at the lowest level.
	 * 
	 * @param row The row that the vertex lies on.
	 * @param col The diagonal line that the vertex lies on, see
	 *            {@link WorldVertex.EnumMeshNeighbor#col}.
	 * @return The vertex at the given coordinate.
	 */
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
			v = new WorldVertex(this, row, col, this.posSeed(row, col), this.getStartData(row, col));
			r.put(col, v);
		}
		return v;
	}
	
	/**
	 * Gets the world vertex at the given row and column and ensures that it is
	 * at {@link WorldVertex#maxLevel}.
	 * 
	 * @param row The row that the vertex lies on.
	 * @param col The diagonal line that the vertex lies on, see
	 *            {@link WorldVertex.EnumMeshNeighbor#col}.
	 * @return The vertex at the given coordinate generated to
	 *         {@link WorldVertex#maxLevel}.
	 */
	public WorldVertex createVertexFull(int row, int col)
	{
		WorldVertex v = this.createVertex(row, col);
		while (v.getLevel() < WorldVertex.maxLevel)
		{
			v.upgrade();
		}
		return v;
	}
	
	private VertexGenData getStartData(int row, int col)
	{
		if (this.parent == null)
		{
			return null;
		}
		int i = (row % 2) + (col % 2) * 2;
		int r = row / 2;
		int c = col / 2;
		switch (i)
		{
			case 0:
				return this.getStartData(this.parent.createVertexFull(r, c), null);
			case 1:
				return this.getStartData(this.parent.createVertexFull(r, c), this.parent.createVertexFull(r + 1, c));
			case 2:
				return this.getStartData(this.parent.createVertexFull(r, c), this.parent.createVertexFull(r, c + 1));
			case 3:
				return this.getStartData(this.parent.createVertexFull(r, c), this.parent.createVertexFull(r + 1, c + 1));
			default:
				return null;
		}
		
	}
	
	private VertexGenData getStartData(WorldVertex parent1, WorldVertex parent2)
	{
		// TODO Mix parent vertices
		return null;
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
