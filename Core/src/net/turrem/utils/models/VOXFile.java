package net.turrem.utils.models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class VOXFile
{
	public int width;
	public int length;
	public int height;

	public byte[] voxels;

	public VOXColor[] colors = new VOXColor[256];

	public static class VOXColor
	{
		byte r;
		byte g;
		byte b;
	}

	private VOXFile()
	{

	}

	public void write(DataOutputStream stream) throws IOException
	{
		writeInt(stream, this.width);
		writeInt(stream, this.length);
		writeInt(stream, this.height);

		stream.write(this.voxels);

		for (int i = 0; i < 256; i++)
		{
			VOXColor c = this.colors[i];

			stream.write(c.r & 0xFF);
			stream.write(c.g & 0xFF);
			stream.write(c.b & 0xFF);
		}
	}

	public static VOXFile read(DataInputStream stream) throws IOException
	{
		VOXFile vox = new VOXFile();

		vox.width = readInt(stream);
		vox.length = readInt(stream);
		vox.height = readInt(stream);

		vox.voxels = new byte[vox.width * vox.length * vox.height];

		for (int i = 0; i < vox.voxels.length; i++)
		{
			vox.voxels[i] = stream.readByte();
		}

		for (int i = 0; i < 256; i++)
		{
			VOXColor c = new VOXColor();

			c.r = stream.readByte();
			c.g = stream.readByte();
			c.b = stream.readByte();

			vox.colors[i] = c;
		}

		return vox;
	}

	private static int readInt(DataInputStream stream) throws IOException
	{
		byte[] bs = new byte[4];
		bs[3] = stream.readByte();
		bs[2] = stream.readByte();
		bs[1] = stream.readByte();
		bs[0] = stream.readByte();

		return ByteBuffer.wrap(bs).getInt();
	}

	private static void writeInt(DataOutputStream stream, int i) throws IOException
	{
		stream.writeByte(i & 0xFF);
		stream.writeByte((i >> 8) & 0xFF);
		stream.writeByte((i >> 16) & 0xFF);
		stream.writeByte((i >> 24) & 0xFF);
	}
}
