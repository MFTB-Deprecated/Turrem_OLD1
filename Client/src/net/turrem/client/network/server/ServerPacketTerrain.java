package net.turrem.client.network.server;

import java.io.DataInput;
import java.io.IOException;

import net.turrem.client.game.material.Material;

public class ServerPacketTerrain extends ServerPacket
{
	public int chunkx;
	public int chunkz;
	public short voff;
	public byte[] hmap;
	public Material[] mats;
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
		this.mats = new Material[data.readByte() & 0xFF];
		for (int i = 0; i < this.mats.length; i++)
		{
			this.mats[i] = Material.getMaterial(data.readShort());
		}
		this.chunk = new byte[256][];
		for (int i = 0; i < 256; i++)
		{
			byte[] col = new byte[data.readByte() & 0xFF];
			data.readFully(col);
			this.chunk[i] = col;
		}
	}
	
	private int getHeight(int x, int z)
	{
		if (x < 0 || x >= 16 || z < 0 || z >= 16)
		{
			return 0;
		}
		return this.hmap[x + z * 16] & 0xFF;
	}
}
