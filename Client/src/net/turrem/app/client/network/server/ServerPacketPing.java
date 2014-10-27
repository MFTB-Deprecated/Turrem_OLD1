package net.turrem.app.client.network.server;

import java.io.DataInput;
import java.io.IOException;

public class ServerPacketPing extends ServerPacket
{
	public int processDelay;
	public int writeDelay;
	public int ping;
	
	private ServerPacketPing(DataInput data, byte type) throws IOException
	{
		super(type);
		this.processDelay = data.readInt();
		this.writeDelay = data.readInt();
		this.ping = (int) (System.nanoTime() - data.readLong());
	}
	
	public static ServerPacketPing create(DataInput data, byte type) throws IOException
	{
		return new ServerPacketPing(data, type);
	}
}
