package net.turrem.server.world;

import java.util.ArrayList;
import java.util.Collection;

import net.turrem.server.world.material.MatStack;

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

	public Collection<MatStack> removeTop(int x, int y)
	{
		Stratum st = this.getTopStratum(x, y);
		int z = this.getHeight(x, y);
		st.addDepth(x, y, -1);
		this.rebuildhmap = true;
		return st.getMat(x, y, z);
	}

	public Collection<MatStack> removeMultiTop(int x, int y, int num, boolean drop)
	{
		ArrayList<MatStack> mats = new ArrayList<MatStack>();
		int z = this.getHeight(x, y);
		if (num < z)
		{
			num = z;
		}
		int n = num;
		while (n > 0)
		{
			Stratum st = this.getTopStratum(x, y);
			int k = st.getDepth(x, y);
			if (n < k)
			{
				k = n;
			}
			if (drop)
			{
				for (int i = 0; i < k; i++)
				{
					mats.addAll(st.getMat(x, y, z--));
				}
			}
			n += st.addDepth(x, y, n);
			this.rebuildhmap = true;
		}
		return mats;
	}

	public Collection<MatStack> removeShapeTop(byte[] grid, boolean drop)
	{
		ArrayList<MatStack> mats = new ArrayList<MatStack>();
		int i = this.strata.size() - 1;
		byte[] rm = grid.clone();
		while (i >= 0)
		{
			Stratum st = this.strata.get(i);
			int sum = 0;
			for (int j = 0; j < 256; j++)
			{
				int g = rm[j] & 0xFF;
				if (g > 0)
				{
					if (drop)
					{
						int x = j % 16;
						int y = i / 16;
						int d = st.getDepth(x, y);
						int z = this.height[j] - (rm[j] & 0xFF) + (grid[j] & 0xFF);
						if (g < d)
						{
							d = g;
						}
						for (int k = 0; k < d; k++)
						{
							mats.addAll(st.getMat(x, y, z));
							z--;
						}
					}

					rm[j] = (byte) ((g - st.removeDepth(j, g)) & 0xFF);
					sum += rm[j] & 0xFF;
				}
			}
			if (sum == 0)
			{
				break;
			}
			i--;
		}
		this.rebuildhmap = true;
		return mats;
	}

	public void placeTop(int x, int y, String id)
	{
		x &= 0x0F;
		y &= 0x0F;
		int last = this.getFirstOpen(x, y, id);
		if (last == -1)
		{
			last = this.strata.size();
			this.strata.add(new Stratum(id));
		}
		Stratum st = this.strata.get(last);
		if (st.addDepth(x, y, 1) != 1)
		{
			st = new Stratum(id);
			this.strata.add(st);
			st.setDepth(x, y, 1);
		}
		this.rebuildhmap = true;
	}
}
