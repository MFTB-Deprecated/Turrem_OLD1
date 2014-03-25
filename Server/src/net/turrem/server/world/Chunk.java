package net.turrem.server.world;

import java.util.ArrayList;

public class Chunk
{
	protected ArrayList<Stratum> strata = new ArrayList<Stratum>();
	protected short[] height;
	protected short[] top;
	private boolean rebuildhmap = false;

	public void buildHeightMap()
	{
		this.rebuildhmap = false;
		this.height = new short[256];
		this.top = new short[256];
		for (int i = 0; i < this.strata.size(); i++)
		{
			byte[] st = this.strata.get(i).getDepthMap();
			for (int j = 0; j < 256; j++)
			{
				this.height[j] += st[j] & 0xFF;
				if (st[j] > 0)
				{
					this.top[j] = (byte) (i & 0xFF);
				}
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
		return this.getTopStratum(x, y).getMateriaId();
	}

	public Stratum getTopStratum(int x, int y)
	{
		x &= 0x0F;
		y &= 0x0F;
		if (this.top == null || this.rebuildhmap)
		{
			this.buildHeightMap();
		}
		int top = this.top[x + y * 16] & 0xFF;
		return this.strata.get(top);
	}

	protected int getFirstOpen(int x, int y, String id)
	{
		x &= 0x0F;
		y &= 0x0F;
		int index = -1;
		for (int i = this.strata.size() - 1; i >= 0; i--)
		{
			Stratum st = this.strata.get(i);
			if (id.equals(st.getMateriaId()))
			{
				index = i;
			}
			if (st.getDepth(x, y) > 0)
			{
				return index;
			}
		}
		return -1;
	}

	public Stratum removeTop(int x, int y)
	{
		Stratum st = this.getTopStratum(x, y);
		st.addDepth(x, y, -1);
		this.rebuildhmap = true;
		return st;
	}

	public void removeMultiTop(int x, int y, int num)
	{
		int n = num;
		while (n > 0)
		{
			Stratum st = this.getTopStratum(x, y);
			n += st.addDepth(x, y, n);
			this.rebuildhmap = true;
		}
	}
	
	public void removeShapeTop(byte[] grid)
	{
		int i = this.strata.size() - 1;
		while (i >= 0)
		{
			Stratum st = this.strata.get(i);
			int sum = 0;
			for (int j = 0; j < 256; j++)
			{
				int g = grid[j] & 0xFF;
				if (g > 0)
				{
					grid[j] = (byte) ((g - st.removeDepth(j, g)) & 0xFF);
					sum += grid[j] & 0xFF;
				}
			}
			if (sum == 0)
			{
				break;
			}
			i--;
		}
		this.rebuildhmap = true;
	}
	
	public void placeTop(int x, int y, String id)
	{
		x &= 0x0F;
		y &= 0x0F;
		int first = this.getFirstOpen(x, y, id);
		if (first == -1)
		{
			first = this.strata.size();
			this.strata.add(new Stratum(id));
		}
		Stratum st = this.strata.get(first);
		if (st.addDepth(x, y, 1) != 1)
		{
			st = new Stratum(id);
			this.strata.add(st);
			st.setDepth(x, y, 1);
		}
		this.rebuildhmap = true;
	}
}
