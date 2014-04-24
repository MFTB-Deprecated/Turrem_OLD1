package net.turrem.server.network.client.request;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.turrem.server.network.client.ClientPacketRequest;

public abstract class Request
{
	public String client;

	public Request(String user)
	{
		this.client = user;
	}

	public static Request getRequest(ClientPacketRequest packet) throws IOException
	{
		if (packet != null)
		{
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(packet.requestData));
			Request req = build(input, packet.requestType, packet.user);
			input.close();
			return req;
		}
		return null;
	}

	private static Request build(DataInputStream in, String type, String user) throws IOException
	{

		switch (type)
		{
			case "chunk":
				return new RequestChunk(type, in);
			default:
				return null;
		}

	}
}
