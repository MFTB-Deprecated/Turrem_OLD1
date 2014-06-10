package net.turrem.client.network.client;

import java.io.DataOutput;
import java.io.IOException;

import net.turrem.client.game.entity.ClientEntity;

public class ClientPacketMove extends ClientPacket
{
	private long ent;
	public float xpos;
	public float zpos;

	public ClientPacketMove()
	{

	}

	public void addEntity(ClientEntity ent)
	{
		this.ent = ent.entityId;
	}

	public void addEntity(long ent)
	{
		this.ent = ent;
	}

	@Override
	protected void writePacket(DataOutput stream) throws IOException
	{
		stream.writeByte(1);
		stream.writeLong(this.ent);
		stream.writeFloat(this.xpos);
		stream.writeFloat(this.zpos);
	}

	@Override
	public byte type()
	{
		return (byte) 0x11;
	}
}
