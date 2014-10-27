package net.turrem.app.client.network.client.request;

import java.io.DataOutput;
import java.io.IOException;

import net.turrem.app.client.network.client.ClientPacketRequest;

public abstract class Request
{
	public abstract String requestType();
	
	public abstract void write(DataOutput stream) throws IOException;
	
	public ClientPacketRequest getPacket()
	{
		return new ClientPacketRequest(this);
	}
}
