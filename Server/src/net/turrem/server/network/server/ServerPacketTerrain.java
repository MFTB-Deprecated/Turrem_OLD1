package net.turrem.server.network.server;

import java.io.DataOutput;
import java.io.IOException;

public class ServerPacketTerrain extends ServerPacket
{
	public int chunkx;
	public int chunkz;
	public short voffset;
	public byte[] hmap;
	public int[] mats;
	public byte[][] chunk;
	
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
			stream.writeInt(this.mats[i]);
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
