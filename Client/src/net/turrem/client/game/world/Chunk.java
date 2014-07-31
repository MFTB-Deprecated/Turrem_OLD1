package net.turrem.client.game.world;

import net.turrem.utils.models.TVFFile;

public class Chunk
{
	public final int chunkx;
	public final int chunkz;
	protected TVFFile tvf;
	protected short voff;
	protected short[] height;
	//private RenderObject render = null;
	private boolean loaded = false;

	public Chunk(int chunkx, int chunkz, TVFFile tvf, byte[] hmap, short voff)
	{
		this.chunkx = chunkx;
		this.chunkz = chunkz;
		this.voff = voff;
		this.tvf = tvf;
		this.height = new short[256];
		for (int i = 0; i < 256; i++)
		{
			this.height[i] = (short) ((hmap[i] & 0xFF) + voff);
		}
	}

	public boolean isLoaded()
	{
		return loaded;
	}

	public void unload()
	{
	}

	public void render()
	{
	}

	public int getHeight(int x, int z)
	{
		x &= 0x0F;
		z &= 0x0F;
		return this.height[x + z * 16];
	}
}