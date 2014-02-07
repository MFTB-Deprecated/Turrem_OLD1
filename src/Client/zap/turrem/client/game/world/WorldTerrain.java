package zap.turrem.client.game.world;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class WorldTerrain
{
	public long seed;

	public TerrainLayer surface;
	public TerrainLayer water;
	public TerrainLayer dry;

	public TerrainLayer heat;
	public TerrainLayer humid;

	public WorldTerrain(long seed)
	{
		this.seed = seed;
	}

	public void generate()
	{
		this.surface = new TerrainLayer(this.seed * 1L, 8, 0.5F);
		this.water = new TerrainLayer(this.seed * 2L, 6, 0.4F);
		this.dry = new TerrainLayer(this.seed * 3L, 6, 0.4F);
		this.heat = new TerrainLayer(this.seed * 4L, 7, 0.5F);
		this.humid = new TerrainLayer(this.seed * 5L, 7, 0.5F);

		this.surface.makeWorld();
		this.water.makeWorld();
		this.dry.makeWorld();
		this.heat.makeWorld();
		this.humid.makeWorld();
	}

	public TerrainChunk getChunk(int x, int y)
	{
		return new TerrainChunk(this.surface.getChunk(x, y), this.water.getChunk(x, y), this.dry.getChunk(x, y), this.heat.getChunk(x, y), this.humid.getChunk(x, y));
	}

	public BufferedImage renderTest(int x, int y, int w, int h)
	{
		BufferedImage img = new BufferedImage(16 * w, 16 * h, BufferedImage.TYPE_INT_RGB);

		for (int ci = 0; ci < w; ci++)
		{
			for (int cj = 0; cj < h; cj++)
			{
				TerrainChunk chunk = this.getChunk(x + ci, y + cj);
				
				for (short i = 0; i < 16; i++)
				{
					for (short j = 0; j < 16; j++)
					{
						float alt = chunk.ajustSurface(i, j, 1.0F, 0.5F, 1.0F);
						int r = 0;
						int g = 0;
						int b = 0;
						if (alt > 0)
						{							
							float riv = chunk.getRiver(i, j, 0.5F, 0.03F, 0.45F, 0.03F);
							if (riv > 0.0F)
							{
								r = (int) (riv * 0);
								g = (int) (riv * 0);
								b = (int) (riv * 32) + 180;
							}
							else
							{
								r = (int) (alt * 64) + 128;
								g = (int) (alt * 64) + 128;
								b = (int) (alt * 64) + 128;
							}
						}
						else
						{
							r = (int) ((1.0F + alt) * 0);
							g = (int) ((1.0F + alt) * 0);
							b = (int) ((1.0F + alt) * 128) + 128;
						}
						Color c = new Color(r, g, b);
						img.setRGB(i + ci * 16, j + cj * 16, c.getRGB());
					}
				}
			}
		}

		return img;
	}
}
