package net.turrem.server.network.server;

import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

import net.turrem.server.world.Chunk;
import net.turrem.server.world.World;
import net.turrem.server.world.material.Material;

public class ServerPacketTerrain extends ServerPacket
{
	public int chunkx;
	public int chunkz;
	public short voffset;
	public byte[] hmap;
	public short[] mats;
	public byte[][] chunk;

	public ServerPacketTerrain(Chunk ch, World world)
	{
		this.chunkx = ch.chunkx;
		this.chunkz = ch.chunkz;
		
		this.create(ch, world);
	}
	
	public ServerPacketTerrain(World world, int chunkx, int chunkz)
	{
		this.chunkx = chunkx;
		this.chunkz = chunkz;

		Chunk ch = world.getChunk(chunkx, chunkz);
		
		this.create(ch, world);
	}
	
	private void create(Chunk ch, World world)
	{
		this.voffset = ch.getMinHeight();

		short[] map = ch.getHeight();

		this.hmap = new byte[256];
		this.chunk = new byte[256][];

		ArrayList<Short> matlist = new ArrayList<Short>();

		for (int i = 0; i < 256; i++)
		{
			this.hmap[i] = (byte) (map[i] - this.voffset);

			int x = chunkx * 16 + (i % 16);
			int z = chunkz * 16 + (i / 16);

			int d = world.getSideDrop(x, z);

			if (d == 0)
			{
				d = 1;
			}

			Material[] cs = ch.coreTerrain(x, d, z);
			byte[] cb = new byte[cs.length];

			for (int j = 0; j < cs.length; j++)
			{
				if (cs[j] != null)
				{
					int ind = matlist.indexOf(cs[j].getNumId());
					if (ind == -1)
					{
						ind = matlist.size();
						matlist.add(cs[j].getNumId());
					}
					cb[j] = (byte) ind;
				}
			}
			this.chunk[i] = cb;
		}

		this.mats = new short[matlist.size()];
		for (int i = 0; i < this.mats.length; i++)
		{
			this.mats[i] = matlist.get(i);
		}
	}

	@Override
	protected void writePacket(DataOutput stream) throws IOException
	{
		stream.writeInt(this.chunkx);
		stream.writeInt(this.chunkz);
		stream.writeShort(this.voffset);
		stream.write(this.hmap);
		stream.writeByte(this.mats.length);
		for (int i = 0; i < this.mats.length; i++)
		{
			stream.writeShort(this.mats[i]);
		}
		for (int i = 0; i < 256; i++)
		{
			byte[] col = this.chunk[i];
			stream.writeByte(col.length);
			stream.write(col);
		}
	}

	@Override
	public byte type()
	{
		return (byte) 0x20;
	}
}
