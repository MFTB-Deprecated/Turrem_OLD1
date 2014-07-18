package net.turrem.server.world.gen;

import java.util.ArrayList;
import java.util.Arrays;

import net.turrem.server.TurremServer;
import net.turrem.server.world.Chunk;
import net.turrem.server.world.Stratum;
import net.turrem.server.world.material.MaterialList;

public class WorldGenBasic extends WorldGen
{
	public long seed;

	public WorldPerlin large;
	public WorldPerlin dirt;
	public WorldPerlin snow;
	public WorldPerlin temp;

	public WorldGenBasic(long seed)
	{
		this.seed = seed;
		this.large = new WorldPerlin(this.seed * 1L, 6, 6, 0.5F);
		this.dirt = new WorldPerlin(this.seed * 4L, 4, 2, 0.8F);
		this.snow = new WorldPerlin(this.seed * 5L, 6, 1, 0.8F);
		this.temp = new WorldPerlin(this.seed * 6L, 4, 3, 0.5F);
	}

	@Override
	public Chunk generateChunk(int chunkx, int chunky)
	{
		Chunk chunk = new Chunk(chunkx, chunky);
		short[] height = new short[256];
		float[] noise0 = this.large.getChunk(chunkx, chunky);
		for (int i = 0; i < 256; i++)
		{
			height[i] = (short) ((noise0[i] * 136) + 4);
		}
		ArrayList<Stratum> strata = this.generateStrata(chunkx, chunky, height);
		chunk.generateWithTop(height, strata);
		return chunk;
	}

	public ArrayList<Stratum> generateStrata(int chunkx, int chunky, short[] hmap)
	{
		float[] noise0 = this.dirt.getChunk(chunkx, chunky);
		float[] noise1 = this.snow.getChunk(chunkx, chunky);
		float[] noise2 = this.temp.getChunk(chunkx, chunky);
		ArrayList<Stratum> strata = new ArrayList<Stratum>();
		{
			byte[] depth = new byte[256];
			Arrays.fill(depth, (byte) 10);
			Stratum stratum = new Stratum(MaterialList.greyStone, depth);
			strata.add(stratum);
		}
		{
			byte[] dirt = new byte[256];
			byte[] lowgrass = new byte[256];
			byte[] highgrass = new byte[256];
			byte[] snow = new byte[256];
			for (int i = 0; i < 256; i++)
			{
				int alt = hmap[i];
				int temp = (int) ((noise2[i] * 15) - 7 + alt);
				int thick;
				if (temp >= 96)
				{
					thick = (int) ((noise1[i] * 8) - 2);
					if (thick < 0)
					{
						thick = 0;
					}
					snow[i] = (byte) thick;
					float mult = (temp - 96) * 0.3F;
					if (mult > 1.0F)
					{
						mult = 1.0F;
					}
					hmap[i] += thick * mult;
				}
				else
				{
					thick = (int) ((noise0[i] * 8) - 2);
					if (thick < 0)
					{
						thick = 0;
					}
					if (temp >= 80)
					{
						if (thick > 1)
						{
							dirt[i] = (byte) (thick - 1);
						}
						if (thick > 0)
						{
							highgrass[i] = (byte) 1;
						}
					}
					else
					{
						if (thick > 1)
						{
							dirt[i] = (byte) (thick - 1);
						}
						if (thick > 0)
						{
							lowgrass[i] = (byte) 1;
						}
					}
					hmap[i] += thick * 0.3F;
				}
			}
			Stratum dirtstratum = new Stratum(MaterialList.dryDirt, dirt);
			Stratum lowstratum = new Stratum(MaterialList.lowGrass, lowgrass);
			Stratum highstratum = new Stratum(MaterialList.highGrass, highgrass);
			Stratum snowstratum = new Stratum(MaterialList.mountainSnow, snow);
			strata.add(dirtstratum);
			strata.add(lowstratum);
			strata.add(highstratum);
			strata.add(snowstratum);
		}
		{
			byte[] sand = new byte[256];
			byte[] beach = new byte[256];
			byte[] ocean = new byte[256];
			for (int i = 0; i < 256; i++)
			{
				int alt = hmap[i];
				int sandt;
				if (alt <= 64)
				{
					sandt = (int) ((noise0[i] * 6) - 2);
					if (sandt < 0)
					{
						sandt = 0;
					}
					sand[i] += sandt;
					int diff = 64 - alt;
					diff += (int) ((noise0[i] * 3) - 1);
					ocean[i] += diff;
					hmap[i] += diff;
				}
				else if (alt <= 68)
				{
					sandt = (int) ((noise0[i] * 7) - 3);
					if (sandt < 0)
					{
						sandt *= -1;
						sand[i] += sandt;
					}
					else
					{
						beach[i] += sandt;
					}
				}

			}
			Stratum sandstratum = new Stratum(MaterialList.dirtySand, sand);
			Stratum beachstratum = new Stratum(MaterialList.normalSand, beach);
			Stratum oceanstratum = new Stratum(MaterialList.oceanWater, ocean);
			strata.add(sandstratum);
			strata.add(beachstratum);
			strata.add(oceanstratum);
		}
		return strata;
	}

	@Override
	public void decorateChunk(Chunk chunk, TurremServer turrem)
	{
		turrem.theLoader.getEntityLoader().processChunkDecorates(chunk, turrem.theWorld);
	}
}
