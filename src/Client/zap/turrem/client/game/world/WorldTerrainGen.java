package zap.turrem.client.game.world;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class WorldTerrainGen
{
	public long seed;

	public TerrainLayer surface;
	public TerrainLayer cont;
	public TerrainLayer mount;

	public WorldTerrainGen(long seed)
	{
		this.seed = seed;
	}

	public void generate()
	{
		this.surface = new TerrainLayer(this.seed * 1L, 6, 0.5F, 4);
		this.cont = new TerrainLayer(this.seed * 2L, 6, 0.4F, 12);
		this.mount = new TerrainLayer(this.seed * 3L, 6, 0.4F, 2);

		this.surface.makeWorld();
		this.cont.makeWorld();
		this.mount.makeWorld();
	}

	public TerrainGenChunk getChunk(int x, int y)
	{
		return new TerrainGenChunk(this.surface.getChunk(x, y), this.cont.getChunk(x, y), this.mount.getChunk(x, y));
	}

	public BufferedImage renderTest(int x, int y, int w, int h)
	{
		BufferedImage img = new BufferedImage(16 * w, 16 * h, BufferedImage.TYPE_INT_RGB);

		for (int ci = 0; ci < w; ci++)
		{
			for (int cj = 0; cj < h; cj++)
			{
				TerrainGenChunk chunk = this.getChunk(x + ci, y + cj);

				for (short i = 0; i < 16; i++)
				{
					for (short j = 0; j < 16; j++)
					{
						float alt = (chunk.ajustSurface(i, j, 1.0F, 0.5F, 0.0F) + 1) / 2;
						int r = (int) (alt * 255);
						int g = (int) (alt * 255);
						int b = (int) (alt * 255);
						Color c = new Color(r, g, b);
						img.setRGB(i + ci * 16, j + cj * 16, c.getRGB());
					}
				}
			}
		}

		return img;
	}
}
