package net.turrem.utils.models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * TVF (Turrem Voxel File)
 */
public class TVFFile
{
	/**
	 * Magic number
	 */
	public byte[] magic;

	/**
	 * The format's magic number
	 */
	public static final byte[] themagic = ("VoxFace").getBytes();

	/**
	 * Turem verion numbers
	 */
	public byte[] turremVersion;

	/**
	 * File verion number
	 */
	public short fileVersion;

	/**
	 * Format version number
	 */
	public static final int theFileVersion = 5;

	public byte width;
	public byte height;
	public byte length;

	/**
	 * The precalculated lighting type for this model. Used for ambient occlusion.
	 * <p>
	 * <li>0: No lighting included</li>
	 * <li>1: Per-vertex lighting included</li>
	 * <li>2: Per-face lighting included</li>
	 * </p>
	 */
	public byte prelit;

	/**
	 * Number of unique colors
	 */
	public short colorNum;

	/**
	 * List of colors
	 */
	public TVFColor[] colors;

	public static class TVFColor
	{
		public byte id;
		public byte r;
		public byte g;
		public byte b;
	}

	/**
	 * Number of dynamic colors
	 */
	public short dynamicColorNum;

	/**
	 * List of dynamic colors
	 */
	public TVFDynamicColor[] dynamicColors;

	/**
	 * A color that can change based on some condition. Not currently implemented.
	 */
	public static class TVFDynamicColor
	{
		public byte id;
		public String name;
	}

	/**
	 * Number of faces
	 */
	public int faceNum;

	/**
	 * List of faces
	 */
	public TVFFace[] faces;

	public static class TVFFace
	{
		public byte x;
		public byte y;
		public byte z;
		public byte dir;
		public byte color;

		// XUp: YUp/ZUp, YDown/ZUp, YDown/ZDown, YUp/ZDown
		// XDown: YUp/ZUp, YUp/ZDown, YDown/ZDown, YDown/ZUp
		// YUp: XUp/ZUp, XUp/ZDown, XDown/ZDown, XDown/ZUp
		// YDown: XUp/ZUp, XDown/ZUp, XDown/ZDown, XUp/ZDown
		// ZUp: XUp/YUp, XDown/YUp, XDown/YDown, XUp/YDown
		// ZDown: XUp/YUp, XUp/YDown, XDown/YDown, XDown/YUp
		public byte[] light = new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };

		public static byte Dir_XUp = (byte) 0x01;
		public static byte Dir_XDown = (byte) 0x02;
		public static byte Dir_YUp = (byte) 0x03;
		public static byte Dir_YDown = (byte) 0x04;
		public static byte Dir_ZUp = (byte) 0x05;
		public static byte Dir_ZDown = (byte) 0x06;

		private final static int[][] vertDir = {new int[] {2, 1, 3, 0}, new int[] {2, 3, 1, 0}, new int[] {2, 3, 1, 0}, new int[] {2, 1, 3, 0}, new int[] {2, 1, 3, 0}, new int[] {2, 3, 1, 0}};
		
		/**
		 * Gets the position of a vertex in this face's vertex array given the relative location of that vertex in the current voxel.
		 * @param x X offset (0 or 1)
		 * @param y Y offset (0 or 1)
		 * @param z Z offset (0 or 1)
		 * @return The position in this face's vertex array.
		 */
		private int getVert(int x, int y, int z)
		{
			int d = (this.dir - 1) / 2;
			int k = 0;
			if (d != 0)
			{
				k <<= 1;
				k |= x;
			}
			if (d != 1)
			{
				k <<= 1;
				k |= y;
			}
			if (d != 2)
			{
				k <<= 1;
				k |= z;
			}
			return vertDir[this.dir - 1][k];
		}
		
		/**
		 * Gets the indices in this face's vertex array of every vertex that matches the given parameters.
		 * @param x X offset (-1 or 1, 0 matches both)
		 * @param y Y offset (-1 or 1, 0 matches both)
		 * @param z Z offset (-1 or 1, 0 matches both)
		 * @return A list of indices in this face's vertex array
		 */
		private int[] getVerts(int x, int y, int z)
		{
			int dir = (this.dir - 1) / 2;
			int i0 = x == 1? 1: 0;
			int j0 = y == 1? 1: 0;
			int k0 = z == 1? 1: 0;
			int i1 = i0 + (x == 0 && dir != 0? 2: 1);
			int j1 = j0 + (y == 0 && dir != 1? 2: 1);
			int k1 = k0 + (z == 0 && dir != 2? 2: 1);
			int[] vrt = new int[(i1 - i0) * (j1 - j0) * (k1 - k0)];
			int v = 0;
			for (int i = i0; i < i1; i++)
			{
				for (int j = j0; j < j1; j++)
				{
					for (int k = k0; k < k1; k++)
					{
						vrt[v++] = this.getVert(i, j, k);
					}
				}
			}
			return vrt;
		}
		
		/**
		 * Multiplies the light values of every vertex that matches the given parameters.
		 * @param x X offset (-1 or 1, 0 matches both)
		 * @param y Y offset (-1 or 1, 0 matches both)
		 * @param z Z offset (-1 or 1, 0 matches both)
		 * @param value The value to multiply the matching vertices' light levels by.
		 */
		public void multiplyLight(int x, int y, int z, float value)
		{
			int[] verts = this.getVerts(x, y, z);
			for (int v : verts)
			{
				this.light[v] = (byte) ((this.light[v] & 0xFF) * value);
			}
		}
		
		/**
		 * Adds to the light values of every vertex that matches the given parameters.
		 * @param x X offset (-1 or 1, 0 matches both)
		 * @param y Y offset (-1 or 1, 0 matches both)
		 * @param z Z offset (-1 or 1, 0 matches both)
		 * @param value The value to add to the matching vertices' light levels.
		 */
		public void addLight(int x, int y, int z, float value)
		{
			int[] verts = this.getVerts(x, y, z);
			for (int v : verts)
			{
				this.light[v] = (byte) ((this.light[v] & 0xFF) + (value * 0xFF));
			}
		}
		
		/**
		 * Sets the light values of every vertex that matches the given parameters.
		 * @param x X offset (-1 or 1, 0 matches both)
		 * @param y Y offset (-1 or 1, 0 matches both)
		 * @param z Z offset (-1 or 1, 0 matches both)
		 * @param value The value to set the matching vertices' light levels to.
		 */
		public void setLight(int x, int y, int z, float value)
		{
			int[] verts = this.getVerts(x, y, z);
			for (int v : verts)
			{
				this.light[v] = (byte) (value * 0xFF);
			}
		}
	}

	private TVFFile()
	{

	}

	/**
	 * Constructs a new TVF file from the given VOX file.
	 * @param vox The VOX file
	 */
	public TVFFile(VOXFile vox)
	{
		this();
		VoxToTvf con = new VoxToTvf(this, vox);
		con.make();
	}

	/**
	 * Creates a TVF file using the given faces and colors.
	 * @param faces The list of faces in the file
	 * @param colors The list of colors in the file
	 */
	public TVFFile(TVFFace[] faces, TVFColor[] colors)
	{
		this();
		this.faceNum = faces.length;
		this.faces = faces;
		this.colorNum = (short) colors.length;
		this.colors = colors;
	}

	/**
	 * Write file
	 * 
	 * @param stream The stream to write too (Should use GZIP compression)
	 * @throws IOException
	 */
	public void write(DataOutputStream stream) throws IOException
	{
		stream.write(themagic);
		stream.write(0);
		stream.write(0);
		stream.write(0);
		stream.writeShort(theFileVersion);

		stream.write(this.width & 0xFF);
		stream.write(this.height & 0xFF);
		stream.write(this.length & 0xFF);

		stream.write(this.prelit & 0xFF);
		stream.writeShort(this.colorNum);

		for (int i = 0; i < this.colorNum; i++)
		{
			TVFColor c = this.colors[i];
			stream.write(c.id & 0xFF);
			stream.write(c.r & 0xFF);
			stream.write(c.g & 0xFF);
			stream.write(c.b & 0xFF);
		}

		if (this.dynamicColors != null)
		{
			stream.writeShort(this.dynamicColorNum);

			for (int i = 0; i < this.dynamicColorNum; i++)
			{
				TVFDynamicColor c = this.dynamicColors[i];
				stream.write(c.id);
				stream.writeUTF(c.name);
			}
		}
		else
		{
			stream.writeShort(0);
		}

		stream.writeInt(this.faceNum);

		for (int i = 0; i < this.faceNum; i++)
		{
			TVFFace f = this.faces[i];
			stream.write(f.x & 0xFF);
			stream.write(f.y & 0xFF);
			stream.write(f.z & 0xFF);
			stream.write(f.dir & 0xFF);
			stream.write(f.color & 0xFF);

			if (this.prelit == 1)
			{
				stream.writeShort(((f.light[0] & 0xF0) << 8) | ((f.light[1] & 0xF0) << 4) | ((f.light[2] & 0xF0) << 0) | ((f.light[3] & 0xF0) >> 4));
			}
			else if (this.prelit == 2)
			{
				int lit = ((f.light[0] & 0xFF) + (f.light[1] & 0xFF) + (f.light[2] & 0xFF) + (f.light[3] & 0xFF)) / 4;
				stream.write(lit);
			}
		}
	}

	/**
	 * Reads a new TVF file
	 * 
	 * @param stream The stream to read from (Should use GZIP compression)
	 * @return The TVF file that was read (null if file is wrong or is wrong
	 *         version)
	 * @throws IOException
	 */
	public static TVFFile read(DataInputStream stream) throws IOException
	{
		TVFFile tvf = new TVFFile();

		tvf.magic = new byte[themagic.length];

		for (int i = 0; i < tvf.magic.length; i++)
		{
			tvf.magic[i] = stream.readByte();
		}

		tvf.turremVersion = new byte[3];

		for (int i = 0; i < tvf.turremVersion.length; i++)
		{
			tvf.turremVersion[i] = stream.readByte();
		}

		tvf.fileVersion = stream.readShort();

		if (tvf.fileVersion != theFileVersion)
		{
			return null;
		}

		tvf.width = stream.readByte();
		tvf.height = stream.readByte();
		tvf.length = stream.readByte();

		tvf.prelit = stream.readByte();

		tvf.colorNum = stream.readShort();
		tvf.colors = new TVFColor[tvf.colorNum];

		for (int i = 0; i < tvf.colorNum; i++)
		{
			TVFColor c = new TVFColor();

			c.id = stream.readByte();
			c.r = stream.readByte();
			c.g = stream.readByte();
			c.b = stream.readByte();

			tvf.colors[i] = c;
		}

		tvf.dynamicColorNum = stream.readShort();
		if (tvf.dynamicColorNum > 0)
		{
			tvf.dynamicColors = new TVFDynamicColor[tvf.dynamicColorNum];

			for (int i = 0; i < tvf.dynamicColorNum; i++)
			{
				TVFDynamicColor c = new TVFDynamicColor();
				c.id = stream.readByte();
				c.name = stream.readUTF();

				tvf.dynamicColors[i] = c;
			}
		}

		tvf.faceNum = stream.readInt();
		tvf.faces = new TVFFace[tvf.faceNum];

		for (int i = 0; i < tvf.faceNum; i++)
		{
			TVFFace f = new TVFFace();

			f.x = stream.readByte();
			f.y = stream.readByte();
			f.z = stream.readByte();
			f.dir = stream.readByte();
			f.color = stream.readByte();
			if (tvf.prelit == 1)
			{
				short lit = stream.readShort();
				f.light[0] = (byte) ((lit >> 12) & 0x0F);
				f.light[1] = (byte) ((lit >> 8) & 0x0F);
				f.light[2] = (byte) ((lit >> 4) & 0x0F);
				f.light[3] = (byte) ((lit >> 0) & 0x0F);
			}
			else if (tvf.prelit == 2)
			{
				byte lit = stream.readByte();
				f.light[0] = lit;
				f.light[1] = lit;
				f.light[2] = lit;
				f.light[3] = lit;
			}

			tvf.faces[i] = f;
		}

		return tvf;
	}
}
