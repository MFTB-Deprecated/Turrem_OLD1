package zap.turrem.client.game.world;

import zap.turrem.core.world.perlin.Perlin;

public class TerrainLayer extends Perlin
{
	private long seed;
	private int numlayers;
	private float mod;
	
	public TerrainLayer(long seed, int num, float mod)
	{
		this.seed = seed;
		this.numlayers = num;
		this.mod = mod;
	}
	
	@Override
	public long getSeed()
	{
		return this.seed;
	}

	@Override
	public float getMult(int layer)
	{
		float mult = 1.0F;
		for (int i = 0; i < layer; i++)
		{
			mult *= this.mod;
		}
		return mult;
	}

	@Override
	public int numLayers()
	{
		return this.numlayers;
	}
}
