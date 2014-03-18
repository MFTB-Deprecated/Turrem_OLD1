package zap.turrem.client.game.world;

import zap.turrem.client.render.engine.RenderEngine;

public class Chunk
{
	public final int chunkx;
	public final int chunky;
	
	public byte[] heightmap = new byte[256];
	public int[] colormap = new int[256];
	
	protected ModelChunk model;
	protected TerrainGenChunk terrain;
	
	private boolean loaded = false;
	
	public Chunk(int chunkx, int chunky, TerrainGenChunk terrain)
	{
		this.chunkx = chunkx;
		this.chunky = chunky;
		this.terrain = terrain;
		
		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				int blue = 0x2119B2;
				int green = 0x4A7746;
				int sand = 0xEAE2AB;
				float h = this.terrain.ajustSurface((short) i, (short) j, 1.0F, 0.5F, 0.0F);
				int c = green;
				if (h < 0)
				{
					c = blue;
				}
				else if (h < 1 / 16.0F)
				{
					c = sand;
				}
				this.colormap[this.getIndex(i, j)] = c;
				int H = (int) (h * 64);
				if (H < 0)
				{
					H = 0;
				}
				this.heightmap[this.getIndex(i, j)] = (byte) H;
			}
		}
	}
	
	public void loadModel(RenderEngine engine)
	{
		if (this.model != null)
		{
			this.model.render.doDelete();
		}
		this.model = new ModelChunk(this, engine);
		this.loaded = true;
	}
	
	public void unloadModel()
	{
		if (this.model != null)
		{
			this.model.render.doDelete();
			this.model = null;
			this.loaded = false;
		}
	}
	
	public int getIndex(int x, int y)
	{
		return (x & 15) * 16 + (y & 15);
	}
	
	public byte getHeight(int x, int y)
	{
		return this.heightmap[this.getIndex(x, y)];
	}
	
	public int getColor(int x, int y)
	{
		return this.colormap[this.getIndex(x, y)];
	}
	
	public void renderChunk()
	{
		if (this.model != null)
		{
			this.model.render();
		}
	}

	public final boolean isLoaded()
	{
		return loaded;
	}
}
