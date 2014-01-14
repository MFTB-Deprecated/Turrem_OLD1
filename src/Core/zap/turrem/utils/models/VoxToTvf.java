package zap.turrem.utils.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class VoxToTvf
{
	public static enum EnumDir
	{
		XUp((byte) 1, (byte) 1, (byte) 0, (byte) 0),
		XDown((byte) 2, (byte) -1, (byte) 0, (byte) 0),
		YUp((byte) 3, (byte) 0, (byte) 1, (byte) 0),
		YDown((byte) 4, (byte) 0, (byte) -1, (byte) 0),
		ZUp((byte) 5, (byte) 0, (byte) 0, (byte) 1),
		ZDown((byte) 6, (byte) 0, (byte) 0, (byte) -1);
		
		public byte ind;
		
		public byte xoff;
		public byte yoff;
		public byte zoff;
		
		EnumDir(byte ind, byte x, byte y, byte z )
		{
			this.ind = ind;
			this.xoff = x;
			this.yoff = y;
			this.zoff = z;
		}
	}
	
	protected TVFFile tvf;
	protected VOXFile vox;
	
	private HashSet<Short> usedColors = new HashSet<Short>();
	private short colorCount = 0;
	private ArrayList<TVFFile.TVFFace> faces = new ArrayList<TVFFile.TVFFace>();
	private boolean[] outside;
	
	public VoxToTvf(TVFFile tvf, VOXFile vox)
	{
		this.tvf = tvf;
		this.vox = vox;
		
		this.outside = new boolean[(this.vox.width + 2) * (this.vox.length + 2) * (this.vox.height + 2)];
	}
	
	public TVFFile make()
	{
		this.build();
		this.convert();
		return this.tvf;
	}

	private void build()
	{
		this.findOutside();

		EnumDir[] dirs = EnumDir.values();

		for (int i = 0; i < this.vox.width; i++)
		{
			for (int j = 0; j < this.vox.height; j++)
			{
				for (int k = 0; k < this.vox.length; k++)
				{
					byte v = this.getVox(i, j, k);
					if (v != (byte) 0xFF)
					{
						for (int d = 0; d < dirs.length; d++)
						{
							EnumDir dir = dirs[d];

							int x = i + dir.xoff;
							int y = j + dir.yoff;
							int z = k + dir.zoff;

							if (this.getOut(x, y, z))
							{
								TVFFile.TVFFace f = new TVFFile.TVFFace();
								f.x = (byte) (i & 0xFF);
								f.y = (byte) (j & 0xFF);
								f.z = (byte) (k & 0xFF);
								f.dir = dir.ind;
								f.color = v;
								this.faces.add(f);
								if (this.usedColors.add((short) (v & 0xFF)))
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
	
	private void convert()
	{		
		int i = 0;
		Iterator<Short> it = this.usedColors.iterator();
		
		this.tvf.colorNum = this.colorCount;
		this.tvf.colors = new TVFFile.TVFColor[this.usedColors.size()];
		
		while(it.hasNext())
		{
			short id = it.next();
			VOXFile.VOXColor c = this.vox.colors[id];
			TVFFile.TVFColor C = new TVFFile.TVFColor();
			C.id = (byte) (id & 0xFF);
			C.r = c.r;
			C.g = c.g;
			C.b = c.b;
			this.tvf.colors[i] = C;
			i++;
		}
		
		this.tvf.faces = this.faces.toArray(new TVFFile.TVFFace[0]);
	}
	
	private byte getVox(int x, int y, int z)
	{
		if (x < 0 || x >= this.vox.width || y < 0 || y >= this.vox.height || z < 0 || z >= this.vox.length)
		{
			return (byte) 0xFF;
		}
		
		return this.vox.voxels[(((y * this.vox.length) + z) * this.vox.width) + x];
	}

	private void findOutside()
	{
		this.tickOutside(-1, -1, -1);
	}

	private void tickOutside(int x, int y, int z)
	{
		this.setOut(x, y, z, true);

		EnumDir[] dirs = EnumDir.values();

		for (int i = 0; i < dirs.length; i++)
		{
			EnumDir dir = dirs[i];

			int X = x + dir.xoff;
			int Y = y + dir.yoff;
			int Z = z + dir.zoff;

			if (this.isInGrid(X, Y, Z, 1))
			{
				if (this.getVox(X, Y, Z) == (byte) 0xFF)
				{
					if (!this.getOut(X, Y, Z, false))
					{
						this.tickOutside(X, Y, Z);
					}
				}
			}
		}
	}
	
	private boolean isInGrid(int x, int y, int z, int d)
	{
		return !(x < 0 - d || x >= this.vox.width + d || y < 0 - d || y >= this.vox.height + d || z < 0 - d || z >= this.vox.length + d);
	}

	private boolean getOut(int x, int y, int z)
	{
		return getOut(x, y, z, true);
	}

	private boolean getOut(int x, int y, int z, boolean def)
	{
		if (!this.isInGrid(x, y, z, 1))
		{
			return def;
		}

		return this.outside[((((y + 1) * this.vox.length) + (z + 1)) * this.vox.width) + (x + 1)];
	}

	private void setOut(int x, int y, int z, boolean out)
	{
		if (!this.isInGrid(x, y, z, 1))
		{
			return;
		}

		this.outside[((((y + 1) * this.vox.length) + (z + 1)) * this.vox.width) + (x + 1)] = out;
	}
}
