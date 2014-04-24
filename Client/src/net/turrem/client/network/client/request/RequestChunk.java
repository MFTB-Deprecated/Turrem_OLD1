package net.turrem.client.network.client.request;

import java.io.DataOutput;
import java.io.IOException;

public class RequestChunk extends Request
{
	public int chunkx;
	public int chunky;

	public RequestChunk(int chunkx, int chunky)
	{
		this.chunkx = chunkx;
		this.chunky = chunky;
	}

	@Override
	public String requestType()
	{
		return "chunk";
	}

	@Override
	public void write(DataOutput stream) throws IOException
	{
		stream.writeInt(this.chunkx);
		stream.writeInt(this.chunky);
	}
}
