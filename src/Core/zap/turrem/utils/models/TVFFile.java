package zap.turrem.utils.models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TVFFile
{
	public byte[] magic;

	public static final byte[] themagic = ("VoxFace").getBytes();

	public byte[] turremVersion;
	public short fileVersion;

	public static final int theFileVersion = 3;

	public short colorNum;

	public TVFColor[] colors;

	public static class TVFColor
	{
		public byte id;
		public byte r;
		public byte g;
		public byte b;
	}

	public short dynamicColorNum;

	public TVFDynamicColor[] dynamicColors;

	public static class TVFDynamicColor
	{
		public byte id;
		public String name;
	}

	public int faceNum;

	public TVFFace[] faces;

	public static class TVFFace
	{
		public byte x;
		public byte y;
		public byte z;
		public byte dir;
		public byte color;

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

	public TVFFile(VOXFile vox)
	{
		this();
		VoxToTvf con = new VoxToTvf(this, vox);
		con.make();
	}

	public void write(DataOutputStream stream) throws IOException
	{
		stream.write(themagic);
		stream.write(0);
		stream.write(0);
		stream.write(0);
		stream.writeShort(theFileVersion);

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
		}
	}

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

			tvf.faces[i] = f;
		}

		return tvf;
	}
}
