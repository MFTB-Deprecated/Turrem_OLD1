package zap.turrem.client.game.world;

import zap.turrem.core.world.perlin.Perlin;

public class TerrainLayer extends Perlin
{
	private long seed;
	private int numlayers;
	private float mod;
	
	private float sumscale;
	
	public TerrainLayer(long seed, int num, float mod)
	{
		this.seed = seed;
		this.numlayers = num;
		this.mod = mod;
		this.calcMax();
	}
	
	@Override
	public long getSeed()
	{
		return this.seed;
	}
	
	private void calcMax()
	{
		float mult = 1.0F;
		float sum = 0.0F;
		for (int i = 0; i < this.numlayers; i++)
		{
			mult *= this.mod;
			sum += mult;
		}
		this.sumscale = sum;
	}

	@Override
	public float getMult(int layer)
	{
		float mult = 1.0F;
		for (int i = 0; i < layer; i++)
		{
			mult *= this.mod;
		}
		return mult / this.sumscale;
	}

	@Override
	public int numLayers()
	{
		return this.numlayers;
	}
}
