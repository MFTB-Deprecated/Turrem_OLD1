package net.turrem.client.network.server;

import java.io.DataInput;
import java.io.IOException;

public class ServerPacketStartingInfo extends ServerPacket
{
	public int startx;
	public int startz;
	
	private ServerPacketStartingInfo(DataInput data, byte type) throws IOException
	{
		super(type);
		this.startx = data.readInt();
		this.startz = data.readInt();
	}
	
	public static ServerPacketStartingInfo create(DataInput data, byte type) throws IOException
	{
		return new ServerPacketStartingInfo(data, type);
	}
}
