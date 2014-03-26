package net.turrem.utils.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import net.turrem.utils.ao.AORay;
import net.turrem.utils.ao.Urchin;
import net.turrem.utils.geo.EnumDir;

public class VoxToTvf
{
	protected static Urchin urchin = new Urchin(32, 128);

	protected TVFFile tvf;
	protected VOXFile vox;

	private HashSet<Short> usedColors = new HashSet<Short>();
	private short colorCount = 0;
	private ArrayList<TVFFile.TVFFace> faces = new ArrayList<TVFFile.TVFFace>();

	private final boolean doao;

	/**
	 * Creates a new converter object
	 * 
	 * @param tvf The TVF file to convert to
	 * @param vox The VOX file to convert from
	 */
	public VoxToTvf(TVFFile tvf, VOXFile vox)
	{
		this.tvf = tvf;
		this.vox = vox;
		this.doao = true;
	}

	/**
	 * Converts the file
	 * 
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
								f.y = (byte) (j & 0xFF);
								f.z = (byte) (k & 0xFF);
								f.dir = dir.ind;
								f.color = v;
								if (this.doao)
								{
									float ao = this.getAO(urchin, i, j, k, dir);
									ao *= 2;
									if (ao > 1)
									{
										ao = 1;
									}
									byte bl = (byte) (ao * 255);
									f.light = new byte[] { bl, bl, bl, bl };
								}
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

	public float getAO(Urchin urchin, int x, int y, int z, EnumDir dir)
	{
		float ao = 0.0F;
		for (int i = 0; i < urchin.rays.length; i++)
		{
			boolean hit = false;
			AORay r = urchin.rays[i];
			for (int j = 0; j < r.points.length; j++)
			{
				int px = r.points[j].x + x;
				int py = r.points[j].y + y;
				int pz = r.points[j].z + z;
				if (px >= this.vox.width || px < 0 || py >= this.vox.height || py < 0 || pz >= this.vox.length || pz < 0)
				{
					break;
				}
				else if (this.getVox(px, py, pz) != (byte) 0xFF)
				{
					hit = true;
					break;
				}
			}
			if (!hit)
			{
				switch (dir)
				{
					case XUp:
						ao += r.xd;
						break;
					case XDown:
						ao += r.xu;
						break;
					case YUp:
						ao += r.yd;
						break;
					case YDown:
						ao += r.yu;
						break;
					case ZUp:
						ao += r.zd;
						break;
					case ZDown:
						ao += r.zu;
						break;
				}
			}
		}
		switch (dir)
		{
			case XUp:
				return ao / urchin.xd;
			case XDown:
				return ao / urchin.xu;
			case YUp:
				return ao / urchin.yd;
			case YDown:
				return ao / urchin.yu;
			case ZUp:
				return ao / urchin.zd;
			case ZDown:
				return ao / urchin.zu;
			default:
				return 0;
		}
	}

	private void convert()
	{
		int i = 0;
		Iterator<Short> it = this.usedColors.iterator();

		this.tvf.width = (byte) this.vox.width;
		this.tvf.height = (byte) this.vox.height;
		this.tvf.length = (byte) this.vox.length;

		this.tvf.colorNum = this.colorCount;
		this.tvf.colors = new TVFFile.TVFColor[this.colorCount];

		while (it.hasNext())
		{
			short id = it.next();
			VOXFile.VOXColor c = this.vox.colors[id];
			TVFFile.TVFColor C = new TVFFile.TVFColor();
			C.id = (byte) (id & 0xFF);
			C.r = (byte) (c.r << 2);
			C.g = (byte) (c.g << 2);
			C.b = (byte) (c.b << 2);
			this.tvf.colors[i] = C;
			i++;
		}

		this.tvf.faces = this.faces.toArray(new TVFFile.TVFFace[0]);
		this.tvf.faceNum = this.tvf.faces.length;

		if (this.doao)
		{
			this.tvf.prelit = 2;
		}
	}

	private boolean isOutside(int x, int y, int z, int d)
	{
		return (x >= this.vox.width + d || x < 0 - d || y >= this.vox.height + d || y < 0 - d || z >= this.vox.length + d || z < 0 - d);
	}

	private byte getVox(int x, int y, int z)
	{
		return this.vox.voxels[(x * this.vox.length + z) * this.vox.height + (this.vox.height - y - 1)];
	}
}
