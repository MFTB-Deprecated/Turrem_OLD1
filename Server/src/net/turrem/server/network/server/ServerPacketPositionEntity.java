package net.turrem.server.network.server;

import java.io.DataOutput;
import java.io.IOException;

public class ServerPacketPositionEntity extends ServerPacket
{
	public long entity;
	public float xpos;
	public float ypos;
	public float zpos;
	
	@Override
	protected void writePacket(DataOutput stream) throws IOException
	{
		stream.writeLong(this.entity);
		stream.writeFloat(this.xpos);
		stream.writeFloat(this.ypos);
		stream.writeFloat(this.zpos);
	}
	
	@Override
	public byte type()
	{
		return (byte) 0x10;
	}
}
