package net.turrem.server.network.server;

import java.io.DataOutput;
import java.io.IOException;

public class ServerPacketKeepAlive extends ServerPacket
{
	@Override
	protected void writePacket(DataOutput stream) throws IOException
	{
	}

	@Override
	public byte type()
	{
		return (byte) 0xFD;
	}
}
