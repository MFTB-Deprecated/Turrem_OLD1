package net.turrem.client.network.server;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;

import net.turrem.client.game.world.Chunk;
import net.turrem.client.game.world.material.Material;
import net.turrem.utils.models.TVFFile;
import net.turrem.utils.models.TVFFile.TVFFace;
import net.turrem.utils.models.TVFFile.TVFColor;

public class ServerPacketTerrain extends ServerPacket
{
	public static final short basePadding = 6;
	public int chunkx;
	public int chunkz;
	public short voff;
	public byte[] hmap;
	public short[] mats;
	public byte[][] chunk;
	
	public ServerPacketTerrain(DataInput data) throws IOException
	{
		this.chunkx = data.readInt();
		this.chunkz = data.readInt();
		this.voff = data.readShort();
		this.hmap = new byte[256];
		for (int i = 0; i < 256; i++)
		{
			this.hmap[i] = data.readByte();
		}
		this.mats = new short[data.readByte() & 0xFF];
		for (int i = 0; i < this.mats.length; i++)
		{
			mats[i] = data.readShort();
		}
		this.chunk = new byte[256][];
		for (int i = 0; i < 256; i++)
		{
			byte[] col = new byte[data.readByte() & 0xFF];
			data.readFully(col);
			this.chunk[i] = col;
		}
	}
	
	public Chunk buildChunk()
	{
		return new Chunk(this.chunkx, this.chunkz, this.buildTVF(), this.hmap, (short) (this.voff - basePadding));
	}
	
	public TVFFile buildTVF()
	{
		ArrayList<TVFFace> faces = new ArrayList<TVFFace>();
		TVFColor[] colors = new TVFColor[this.mats.length];
		
		for (int i = 0; i < colors.length; i++)
		{
			Material mat = Material.getMaterial(this.mats[i]);
			int cint = 0xFFFFFF;
			if (mat != null)
			{
				cint = mat.getColor();
			}
			TVFColor color = new TVFColor();
			color.id = (byte) i;
			color.b = (byte) ((cint >> 0) & 0xFF);
			color.g = (byte) ((cint >> 8) & 0xFF);
			color.r = (byte) ((cint >> 16) & 0xFF);
			colors[i] = color;
		}
		
		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				this.makeCore(i, j, faces);
			}
		}
		
		TVFFace[] far = new TVFFace[faces.size()];
		far = faces.toArray(far);
		TVFFile tvf = new TVFFile(far, colors);
		return tvf;
	}
	
	private int getHeight(int x, int z)
	{
		if (x < 0 || x >= 16 || z < 0 || z >= 16)
		{
			return Integer.MIN_VALUE;
		}
		return this.hmap[x + z * 16] & 0xFF;
	}
	
	private void makeCore(int x, int z, ArrayList<TVFFace> faces)
	{
		int ind = x + z * 16;
		byte[] col = this.chunk[ind];
		int y = this.getHeight(x, z);
		if (col != null && col.length > 0)
		{
			TVFFace top = new TVFFace();
			top.color = col[0];
			top.x = (byte) x;
			top.z = (byte) z;
			top.y = (byte) y;
			top.y += basePadding;
			top.dir = TVFFace.Dir_YUp;
			faces.add(top);
			
			int h;
			
			h = this.getHeight(x + 1, z);
			for (int i = 0; i < col.length; i++)
			{
				if (y - i > h)
				{
					TVFFace f = new TVFFace();
					f.color = col[i];
					f.x = (byte) x;
					f.z = (byte) z;
					f.y = (byte) (y - i);
					f.y += basePadding;
					f.dir = TVFFace.Dir_XUp;
					faces.add(f);
				}
				else
				{
					break;
				}
			}
			
			h = this.getHeight(x - 1, z);
			for (int i = 0; i < col.length; i++)
			{
				if (y - i > h)
				{
					TVFFace f = new TVFFace();
					f.color = col[i];
					f.x = (byte) x;
					f.z = (byte) z;
					f.y = (byte) (y - i);
					f.y += basePadding;
					f.dir = TVFFace.Dir_XDown;
					faces.add(f);
				}
				else
				{
					break;
				}
			}
			
			h = this.getHeight(x, z + 1);
			for (int i = 0; i < col.length; i++)
			{
				if (y - i > h)
				{
					TVFFace f = new TVFFace();
					f.color = col[i];
					f.x = (byte) x;
					f.z = (byte) z;
					f.y = (byte) (y - i);
					f.y += basePadding;
					f.dir = TVFFace.Dir_ZUp;
					faces.add(f);
				}
				else
				{
					break;
				}
			}
			
			h = this.getHeight(x, z - 1);
			for (int i = 0; i < col.length; i++)
			{
				if (y - i > h)
				{
					TVFFace f = new TVFFace();
					f.color = col[i];
					f.x = (byte) x;
					f.z = (byte) z;
					f.y = (byte) (y - i);
					f.y += basePadding;
					f.dir = TVFFace.Dir_ZDown;
					faces.add(f);
				}
				else
				{
					break;
				}
			}
		}
	}
}
