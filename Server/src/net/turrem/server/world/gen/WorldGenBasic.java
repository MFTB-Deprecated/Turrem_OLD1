package net.turrem.server.world.gen;

import java.util.ArrayList;

import net.turrem.server.TurremServer;
import net.turrem.server.world.Chunk;
import net.turrem.server.world.Stratum;

public class WorldGenBasic extends WorldGen
{
	public long seed;

	public WorldPerlin large;
	public WorldPerlin detail;
	public WorldPerlin mult;
	public WorldPerlin dirt;

	public WorldGenBasic(long seed)
	{
		this.seed = seed;
		this.large = new WorldPerlin(this.seed * 1L, 6, 6, 0.5F);
		this.mult = new WorldPerlin(this.seed * 2L, 6, 6, 0.5F);
		this.detail = new WorldPerlin(this.seed * 3L, 6, 2, 0.5F);
		this.dirt = new WorldPerlin(this.seed * 4L, 6, 2, 0.8F);
	}

	@Override
	public ArrayList<Stratum> generateChunk(int chunkx, int chunky)
	{
		ArrayList<Stratum> strata = new ArrayList<Stratum>();
		float[] la = this.large.getChunk(chunkx, chunky);
		float[] de = this.detail.getChunk(chunkx, chunky);
		float[] di = this.dirt.getChunk(chunkx, chunky);
		float[] mu = this.mult.getChunk(chunkx, chunky);
		int[] height = new int[256];
		for (int i = 0; i < 256; i++)
		{
			int hi = (int) (la[i] * 192 + (1.0F - de[i] * de[i]) * 32 * mu[i]);
			if (hi < 1)
			{
				hi = 1;
			}
			int j = 0;
			int h = hi;
			height[i] += hi;
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
		strata.add(new Stratum("dirt"));
		int grassl = strata.size();
		strata.add(new Stratum("grass"));
		int sandl = strata.size();
		strata.add(new Stratum("sand"));
		int waterl = strata.size();
		for (int i = 0; i < 256; i++)
		{
			int hi = height[i];
			if (hi > 104)
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
				height[i] += d;
			}
			else
			{
				int d = (int) (di[i] * 12 - 4);
				if (d > 0)
				{
					strata.get(sandl).depth[i] = (byte) d;
					height[i] += d;
				}
			}
			hi = height[i];
			if (hi <= 96)
			{
				int h = 96 - hi;
				int j = waterl;
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
						strata.add(new Stratum("water"));
					}
					strata.get(j).depth[i] = (byte) H;
					j++;
				}
			}
		}
		return strata;
	}

	@Override
	public void decorateChunk(Chunk chunk, TurremServer turrem)
	{
		turrem.theLoader.getEntityLoader().processChunkDecorates(chunk, turrem.theWorld);
	}
}
