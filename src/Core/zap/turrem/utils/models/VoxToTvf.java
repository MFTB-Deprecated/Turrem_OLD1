package zap.turrem.utils.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class VoxToTvf
{
	/**
	 * Enum for easily using the 6 different faces of a cube
	 */
	public static enum EnumDir
	{
		XUp((byte) 1, (byte) 1, (byte) 0, (byte) 0),
		XDown((byte) 2, (byte) -1, (byte) 0, (byte) 0),
		YUp((byte) 4, (byte) 0, (byte) 1, (byte) 0),
		YDown((byte) 3, (byte) 0, (byte) -1, (byte) 0),
		ZUp((byte) 5, (byte) 0, (byte) 0, (byte) 1),
		ZDown((byte) 6, (byte) 0, (byte) 0, (byte) -1);

		public byte ind;

		public byte xoff;
		public byte yoff;
		public byte zoff;

		EnumDir(byte ind, byte x, byte y, byte z)
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

	/**
	 * Creates a new converter object
	 * @param tvf The TVF file to convert to
	 * @param vox The VOX file to convert from
	 */
	public VoxToTvf(TVFFile tvf, VOXFile vox)
	{
		this.tvf = tvf;
		this.vox = vox;
	}

	/**
	 * Converts the file
	 * @return Converted TVF file
	 */
	public TVFFile make()
	{
		this.build();
		this.convert();
		return this.tvf;
	}

	private void build()
	{
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

							if (this.isOutside(x, y, z, 0) || this.getVox(x, y, z) == (byte) 0xFF)
							{
								TVFFile.TVFFace f = new TVFFile.TVFFace();
								f.x = (byte) (i & 0xFF);
								f.y = (byte) ((vox.height - j) & 0xFF);
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
		this.tvf.colors = new TVFFile.TVFColor[this.colorCount];

		while (it.hasNext())
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
		this.tvf.faceNum = this.tvf.faces.length;
	}
	
	private boolean isOutside(int x, int y, int z, int d)
	{
		return (x >= this.vox.width + d || x < 0 - d || y >= this.vox.height + d || y < 0 - d || z >= this.vox.length + d || z < 0 - d);
	}

	private byte getVox(int x, int y, int z)
	{
		return this.vox.voxels[(x * this.vox.length + z) * this.vox.height + y];
	}
}
