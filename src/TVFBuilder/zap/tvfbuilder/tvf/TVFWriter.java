package zap.tvfbuilder.tvf;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import zap.tvfbuilder.Color;
import zap.tvfbuilder.tvf.Face.EnumDir;
import zap.tvfbuilder.vxl.VoxelGrid;

public class TVFWriter
{
	protected VoxelGrid grid;

	protected HashSet<Short> usedColors = new HashSet<Short>();
	protected short colorCount = 0;
	protected ArrayList<Face> faces = new ArrayList<Face>();
	protected boolean[] outside;

	public TVFWriter(VoxelGrid grid)
	{
		this.grid = grid;
		this.outside = new boolean[this.grid.getHeight() * this.grid.getLength() * this.grid.getWidth()];
	}

	public void build()
	{
		this.findOutside();

		EnumDir[] dirs = Face.EnumDir.values();

		for (int i = 0; i < this.grid.getWidth(); i++)
		{
			for (int j = 0; j < this.grid.getHeight(); j++)
			{
				for (int k = 0; k < this.grid.getLength(); k++)
				{
					short v = (short) this.grid.getVox(i, j, k);
					if (v != 255)
					{
						for (int d = 0; d < dirs.length; d++)
						{
							EnumDir dir = dirs[d];

							int x = i + dir.xoff;
							int y = j + dir.yoff;
							int z = k + dir.zoff;

							if (this.getOut(x, y, z))
							{
								this.faces.add(new Face((short) i, (short) j, (short) k, dir, v));
								if (this.usedColors.add(v))
								{
									this.colorCount++;
								}
							}
						}
					}
				}
			}
		}
	}

	public void findOutside()
	{
		this.tickOutside(-1, -1, -1);
	}

	private void tickOutside(int x, int y, int z)
	{
		this.setOut(x, y, z, true);

		EnumDir[] dirs = Face.EnumDir.values();

		for (int i = 0; i < dirs.length; i++)
		{
			EnumDir dir = dirs[i];

			int X = x + dir.xoff;
			int Y = y + dir.yoff;
			int Z = z + dir.zoff;

			if (this.isInGrid(X, Y, Z, 1))
			{
				if (this.grid.getVox(X, Y, Z) == 255)
				{
					if (!this.getOut(X, Y, Z, false))
					{
						this.tickOutside(X, Y, Z);
					}
				}
			}
		}
	}

	public boolean isInGrid(int x, int y, int z)
	{
		return !(x < 0 || x >= this.grid.getWidth() || y < 0 || y >= this.grid.getHeight() || z < 0 || z >= this.grid.getLength());
	}

	public boolean isInGrid(int x, int y, int z, int d)
	{
		return !(x < 0 - d || x >= this.grid.getWidth() + d || y < 0 - d || y >= this.grid.getHeight() + d || z < 0 - d || z >= this.grid.getLength() + d);
	}

	public boolean getOut(int x, int y, int z)
	{
		return getOut(x, y, z, true);
	}

	public boolean getOut(int x, int y, int z, boolean def)
	{
		if (!this.isInGrid(x, y, z))
		{
			return def;
		}

		return this.outside[(((y * this.grid.getLength()) + z) * this.grid.getWidth()) + x];
	}

	public void setOut(int x, int y, int z, boolean out)
	{
		if (!this.isInGrid(x, y, z))
		{
			return;
		}

		this.outside[(((y * this.grid.getLength()) + z) * this.grid.getWidth()) + x] = out;
	}
	
	public void write(DataOutputStream stream) throws IOException
	{
		Iterator<Short> it = this.usedColors.iterator();
		
		stream.writeShort(this.colorCount);
		
		while (it.hasNext())
		{
			short v = it.next();
			
			stream.write((byte) (v + Byte.MIN_VALUE));
			
			Color c = this.grid.getColor(v);
			
			stream.write(c.r);
			stream.write(c.g);
			stream.write(c.b);
		}
		
		stream.writeInt(this.faces.size());
		
		for (int i = 0; i < this.faces.size(); i++)
		{
			Face f = this.faces.get(i);
			
			stream.write((byte) (f.x + Byte.MIN_VALUE));
			stream.write((byte) (f.y + Byte.MIN_VALUE));
			stream.write((byte) (f.z + Byte.MIN_VALUE));
			
			stream.write((byte) f.dir.ind);
			
			stream.write((byte) (f.color + Byte.MIN_VALUE));
		}
	}
}
