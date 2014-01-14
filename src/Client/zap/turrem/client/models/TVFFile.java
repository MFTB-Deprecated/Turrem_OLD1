package zap.turrem.client.models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TVFFile
{
	public short colorNum;
	
	public TVFColor[] colors;
	
	public static class TVFColor
	{
		public byte id;
		public byte r;
		public byte g;
		public byte b;
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
	
	public void write(DataOutputStream stream) throws IOException
	{
		stream.writeShort(this.colorNum);
		
		for (int i = 0; i < this.colorNum; i++)
		{
			TVFColor c = this.colors[i];
			stream.write(c.id & 0xFF);
			stream.write(c.r & 0xFF);
			stream.write(c.g & 0xFF);
			stream.write(c.b & 0xFF);
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
		
		tvf.colorNum = stream.readShort();
		tvf.colors = new TVFColor[tvf.colorNum];
		
		for (int i = 0; i < tvf.colorNum; i++)
		{
			TVFColor c = new TVFColor();
			
			c.id = stream.readByte();
			c.r = stream.readByte();
			c.g = stream.readByte();
			c.b = stream.readByte();
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
		}
		
		return tvf;
	}
}
