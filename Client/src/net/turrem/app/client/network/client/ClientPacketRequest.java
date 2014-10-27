package net.turrem.app.client.network.client;

import java.io.DataOutput;
import java.io.IOException;

import net.turrem.app.client.network.client.request.Request;

public class ClientPacketRequest extends ClientPacket
{
	public Request req;
	
	public ClientPacketRequest(Request request)
	{
		this.req = request;
	}
	
	@Override
	protected void writePacket(DataOutput stream) throws IOException
	{
		byte[] typebytes = this.req.requestType().getBytes("UTF-8");
		stream.writeByte(typebytes.length);
		stream.write(typebytes);
		this.req.write(stream);
	}
	
	@Override
	public byte type()
	{
		return (byte) 0x30;
	}
}
