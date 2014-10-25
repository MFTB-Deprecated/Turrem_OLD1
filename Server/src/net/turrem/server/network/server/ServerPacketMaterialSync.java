package net.turrem.server.network.server;

import java.io.DataOutput;
import java.io.IOException;

import net.turrem.server.world.material.Material;

public class ServerPacketMaterialSync extends ServerPacket
{
	public Material material;
	
	@Override
	protected void writePacket(DataOutput stream) throws IOException
	{
		stream.writeShort(this.material.getNumId());
		byte[] namebytes = this.material.getName().getBytes("UTF-8");
		stream.writeByte(namebytes.length);
		stream.write(namebytes);
		int color = this.material.getColorInt();
		stream.writeByte((color >> 16) & 0xFF);
		stream.writeByte((color >> 8) & 0xFF);
		stream.writeByte((color >> 0) & 0xFF);
	}
	
	@Override
	public byte type()
	{
		return (byte) 0x21;
	}
}
