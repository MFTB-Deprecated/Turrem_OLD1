package net.turrem.app.client.network.server;

import java.io.DataInput;

public class NullPacket extends ServerPacket
{
	public int length;
	public DataInput data;
	
	public NullPacket(byte packetType, int length, DataInput data)
	{
		super(packetType);
		this.length = length;
		this.data = data;
	}
}
