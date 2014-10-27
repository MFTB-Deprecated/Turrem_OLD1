package net.turrem.app.client.network.client;

import java.io.DataOutput;
import java.io.IOException;

public class ClientPacketKeepAlive extends ClientPacket
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
