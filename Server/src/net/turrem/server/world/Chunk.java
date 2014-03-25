package net.turrem.server.world;

import java.util.ArrayList;

public class Chunk
{
	protected ArrayList<Stratum> strata = new ArrayList<Stratum>();
	protected short[] height;
	private boolean rebuildhmap = false;
	
	public void buildHeightMap()
	{
		this.height = new short[256];
		for (int i = 0; i < this.strata.size(); i++)
		{
			byte[] st = this.strata.get(i).getDepthMap();
			for (int j = 0; j < 256; j++)
			{
				this.height[j] += st[j] & 0xFF;
			}
		}
	}
	
	public short[] getHeightMap()
	{
		if (this.height == null || this.rebuildhmap)
		{
			this.buildHeightMap();
		}
		return this.height;
	}
	
	public short getHeight(int x, int y)
	{
		x &= 0x0F;
		y &= 0x0F;
		if (this.height == null || this.rebuildhmap)
		{
			this.buildHeightMap();
		}
		return this.height[x + y * 16];
	}
	
	public String getTopMaterial(int x, int y)
	{
		x &= 0x0F;
		y &= 0x0F;
		for (int i = this.strata.size() - 1; i >= 0; i--)
		{
			Stratum st = this.strata.get(i);
			if (st.getDepth(x, y) > 0)
			{
				return st.getMateriaId();
			}
		}
		return null;
	}
}
