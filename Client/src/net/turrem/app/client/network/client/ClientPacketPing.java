package net.turrem.app.client.network.client;

import java.io.DataOutput;
import java.io.IOException;

public class ClientPacketPing extends ClientPacket
{
	@Override
	protected void writePacket(DataOutput stream) throws IOException
	{
		stream.writeLong(System.nanoTime());
	}
	
	@Override
	public byte type()
	{
		return (byte) 0x32;
	}
}
