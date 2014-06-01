package net.turrem.client.network.server;

import java.io.DataInput;
import java.io.IOException;

public class ServerPacketPing extends ServerPacket
{
	public int processDelay;
	public int writeDelay;
	public int ping;
	
	public ServerPacketPing(DataInput data) throws IOException
	{
		this.processDelay = data.readInt();
		this.writeDelay = data.readInt();
		this.ping = (int) (System.nanoTime() - data.readLong());
	}
}
