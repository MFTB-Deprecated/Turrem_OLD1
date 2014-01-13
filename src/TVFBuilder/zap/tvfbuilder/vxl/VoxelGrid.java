package zap.tvfbuilder.vxl;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import zap.tvfbuilder.Color;

public class VoxelGrid
{
	private short[] grid;
	private int width;
	private int height;
	private int length;
	private Color[] colors;
	
	public VoxelGrid(int w, int h, int l)
	{
		this.width = w;
		this.height = h;
		this.length = l;
		
		this.grid = new short[w * h * l];
		this.colors = new Color[255];
	}

	public Color getColor(int i)
	{
		return this.colors[i];
	}
	
	public int getVox(int x, int y, int z)
	{
		if (x < 0 || x >= this.width || y < 0 || y >= this.height || z < 0 || z >= this.length)
		{
			return 255;
		}
		
		return this.grid[(((y * this.length) + z) * this.width) + x];
	}
	
	public final int getWidth()
	{
		return width;
	}

	public final int getHeight()
	{
		return height;
	}

	public final int getLength()
	{
		return length;
	}
	
	public static VoxelGrid getGrid(DataInputStream stream) throws IOException
	{
		int w = readInt(stream);
		int l = readInt(stream);
		int h = readInt(stream);
		
		VoxelGrid grid = new VoxelGrid(w, h, l);
		
		for (int i = 0; i < grid.grid.length; i++)
		{
			grid.grid[i] = (short) stream.read();
		}
		
		for (int i = 0; i < grid.colors.length; i++)
		{
			int r = stream.read();
			int g = stream.read();
			int b = stream.read();
			
			grid.colors[i] = new Color(r, g, b);
		}
		
		return grid;
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
}
