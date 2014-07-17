package net.turrem.server.world;

import net.turrem.server.world.material.Material;

public class Stratum
{
	public Material material;
	public byte[] depth = new byte[256];
	private int volume = 0;
	private boolean rebuildVolume = true;

	public Stratum(Material material, byte[] depth)
	{
		this.material = material;
		this.depth = depth;
	}

	public void sumHeight(short[] height)
	{
		for (int i = 0; i < 256; i++)
		{
			height[i] += this.depth[i] & 0xFF;
		}
	}
	
	public void subHeight(short[] height)
	{
		for (int i = 0; i < 256; i++)
		{
			height[i] -= this.depth[i] & 0xFF;
		}
	}

	protected void buildVolume()
	{
		if (this.rebuildVolume)
		{
			this.rebuildVolume = false;
			this.volume = 0;
			for (int i = 0; i < 256; i++)
			{
				this.volume += this.depth[i] & 0xFF;
			}
		}
	}

	public int getVolume()
	{
		this.buildVolume();
		return volume;
	}

	public short getDepth(int x, int z)
	{
		return (short) (this.depth[x + z * 16] & 0xFF);
	}

	public Material getMaterial()
	{
		return this.material;
	}
}
