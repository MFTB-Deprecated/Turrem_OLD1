package net.turrem.server.network.server;

import java.io.DataOutput;
import java.io.IOException;

public class ServerPacketInfo extends ServerPacket
{
	@Override
	protected void writePacket(DataOutput stream) throws IOException
	{
		stream.write(0x00);
	}

	@Override
	public byte type()
	{
		return (byte) 0x31;
	}
}
