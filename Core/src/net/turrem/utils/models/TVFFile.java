package net.turrem.utils.models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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

		public byte[] light = new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };

		public static byte Dir_XUp = (byte) 0x01;
		public static byte Dir_XDown = (byte) 0x02;
		public static byte Dir_YUp = (byte) 0x03;
		public static byte Dir_YDown = (byte) 0x04;
		public static byte Dir_ZUp = (byte) 0x05;
		public static byte Dir_ZDown = (byte) 0x06;
	}

	private TVFFile()
	{

	}

	/**
	 * Construct TVF file from VOX file
	 * 
	 * @param vox The VOX file
	 */
	public TVFFile(VOXFile vox)
	{
		this();
		VoxToTvf con = new VoxToTvf(this, vox);
		con.make();
	}

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
