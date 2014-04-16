package net.turrem.server.network.server;

import java.io.DataOutput;
import java.io.IOException;

public class ServerPacketTerrain extends ServerPacket
{
	@Override
	protected void writePacket(DataOutput stream) throws IOException
	{
		stream.writeUTF("unimplemented");
	}

	@Override
	public byte type()
	{
		return (byte) 0x20;
	}
}
