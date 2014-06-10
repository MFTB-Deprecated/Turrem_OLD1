package net.turrem.client.network.server;

import java.io.DataInput;
import java.io.IOException;

public class ServerPacketRemoveEntity extends ServerPacket
{
	public static enum EntityRemoveType
	{
		KILL,
		UNLOAD,
		OFFMAP;
	}
	
	public long entityId;
	public EntityRemoveType removeType;
	
	private ServerPacketRemoveEntity(DataInput data, byte type) throws IOException
	{
		super(type);
		this.entityId = data.readLong();
		this.removeType = EntityRemoveType.values()[data.readByte() & 0xFF];
	}

	public static ServerPacketRemoveEntity create(DataInput data, byte packetType) throws IOException
	{
		return new ServerPacketRemoveEntity(data, packetType);
	}
}
