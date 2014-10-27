package net.turrem.app.server.world.gen;

import net.turrem.app.utils.perlin.Perlin;

public class WorldPerlin extends Perlin
{
	private long seed;
	private int layers;
	private int scale;
	private float theta;
	private float sumscale;
	
	public WorldPerlin(long seed, int layers, int scale, float theta)
	{
		super();
		this.seed = seed;
		this.layers = layers;
		this.scale = scale;
		this.theta = theta;
		this.calcMax();
		this.makeWorld();
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
		for (int i = 0; i < this.layers; i++)
		{
			mult *= this.theta;
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
			mult *= this.theta;
		}
		return mult / this.sumscale;
	}
	
	@Override
	public int getLastScale()
	{
		return this.scale;
	}
	
	@Override
	public int numLayers()
	{
		return this.layers;
	}
}
