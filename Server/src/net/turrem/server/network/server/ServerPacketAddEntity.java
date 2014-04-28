package net.turrem.server.network.server;

import java.io.DataOutput;
import java.io.IOException;

import net.turrem.server.entity.Entity;

public class ServerPacketAddEntity extends ServerPacket
{
	Entity entity;

	public ServerPacketAddEntity(Entity entity)
	{
		this.entity = entity;
	}

	@Override
	protected void writePacket(DataOutput stream) throws IOException
	{
		stream.writeInt((int) this.entity.entityIdentifier);
		byte[] typebytes = this.entity.getEntityType().getBytes("UTF-8");
		stream.writeByte(typebytes.length);
		stream.write(typebytes);
		stream.writeFloat((float) this.entity.x);
		stream.writeFloat((float) this.entity.y);
		stream.writeFloat((float) this.entity.z);
		entity.writeExtraData(stream);
	}

	@Override
	public byte type()
	{
		return (byte) 0x90;
	}
}
