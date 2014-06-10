package net.turrem.server.network.server;

import java.io.DataOutput;
import java.io.IOException;

public class ServerPacketEntityRemove extends ServerPacket
{
	public static enum EntityRemoveType
	{
		KILL,
		UNLOAD,
		OFFMAP;
	}
	
	public EntityRemoveType removeType;
	public long entityId;
	
	public ServerPacketEntityRemove(EntityRemoveType removeType, long entityId)
	{
		this.removeType = removeType;
		this.entityId = entityId;
	}

	@Override
	protected void writePacket(DataOutput stream) throws IOException
	{
		stream.writeLong(this.entityId);
		stream.writeByte(this.removeType.ordinal());
	}

	@Override
	public byte type()
	{
		return (byte) 0x91;
	}
}
