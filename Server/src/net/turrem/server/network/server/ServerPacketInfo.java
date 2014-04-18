package net.turrem.server.network.server;

import java.io.DataOutput;
import java.io.IOException;

import net.turrem.server.TurremServer;

public class ServerPacketInfo extends ServerPacket
{
	public float tps;
	
	public ServerPacketInfo(TurremServer turrem)
	{
		this.tps = turrem.getLastTPS();
	}
	
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
