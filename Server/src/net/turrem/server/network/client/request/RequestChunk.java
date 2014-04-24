package net.turrem.server.network.client.request;

import java.io.DataInput;
import java.io.IOException;

public class RequestChunk extends Request
{
	public int chunkx;
	public int chunky;
	
	public RequestChunk(String user, DataInput in) throws IOException
	{
		super(user);
		this.chunkx = in.readInt();
		this.chunky = in.readInt();
	}
}
