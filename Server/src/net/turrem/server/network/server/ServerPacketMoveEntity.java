package net.turrem.server.network.server;

import java.io.DataOutput;
import java.io.IOException;

public class ServerPacketMoveEntity extends ServerPacketPositionEntity
{
	public short movetime;
	
	@Override
	protected void writePacket(DataOutput stream) throws IOException
	{
		super.writePacket(stream);
		stream.writeShort(this.movetime);
	}
}
