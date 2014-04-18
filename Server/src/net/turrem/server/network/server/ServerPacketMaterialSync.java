package net.turrem.server.network.server;

import java.io.DataOutput;
import java.io.IOException;

import net.turrem.server.world.material.Material;

public class ServerPacketMaterialSync extends ServerPacket
{
	public Material mat;
	
	@Override
	protected void writePacket(DataOutput stream) throws IOException
	{
		stream.writeShort(mat.getNumId());
		byte[] frombytes = this.mat.id.getBytes("UTF-8");
		stream.writeByte(frombytes.length);
		stream.write(frombytes);
	}

	@Override
	public byte type()
	{
		return (byte) 0x21;
	}
}
