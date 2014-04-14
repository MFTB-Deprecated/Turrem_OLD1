package net.turrem.server.world.gen;

import java.util.ArrayList;

import net.turrem.server.world.Stratum;

public class WorldGenBasic extends WorldGen
{
	public long seed;
	
	public WorldPerlin large;
	public WorldPerlin detail;
	public WorldPerlin dirt;
	
	public WorldGenBasic(long seed)
	{
		this.seed = seed;
		this.large = new WorldPerlin(this.seed * 1L, 6, 6, 0.5F);
		this.detail = new WorldPerlin(this.seed * 2L, 6, 2, 0.5F);
		this.dirt = new WorldPerlin(this.seed * 3L, 6, 2, 0.5F);
	}
	
	@Override
	public ArrayList<Stratum> generateChunk(int chunkx, int chunky)
	{
		ArrayList<Stratum> strata = new ArrayList<Stratum>();
		float[] la = this.large.getChunk(chunkx, chunky);
		float[] de = this.detail.getChunk(chunkx, chunky);
		float[] di = this.dirt.getChunk(chunkx, chunky);
		for (int i = 0; i < 256; i++)
		{
			int h = (int) (la[i] * 512 + de[i] * 48);
			int j = 0;
			while (h > 0)
			{
				int H = h;
				if (H > 255)
				{
					H = 255;
				}
				h -= H;
				if (j >= strata.size())
				{
					strata.add(new Stratum("stone"));
				}
				strata.get(j).depth[i] = (byte) H;
				j++;
			}
		}
		int dirtl = strata.size();
		int grassl = strata.size() + 1;
		strata.add(new Stratum("dirt"));
		strata.add(new Stratum("grass"));
		for (int i = 0; i < 256; i++)
		{
			int d = (int) (di[i] * 12 - 4);
			if (d < 0)
			{
				d = 0;
			}
			if (d > 0)
			{
				strata.get(grassl).depth[i] = (byte) 1;
			}
			if (d > 1)
			{
				strata.get(dirtl).depth[i] = (byte) (d - 1);
			}
		}
		return strata;
	}

}
