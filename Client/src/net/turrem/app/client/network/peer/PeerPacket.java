package net.turrem.app.client.network.peer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class PeerPacket
{
	protected abstract void writePacket(DataOutput stream) throws IOException;
	
	protected abstract void readPacket(DataInput input) throws IOException;
	
	public void write(DataOutput stream) throws IOException
	{
		
	}
	
	protected void read(DataInput input) throws IOException
	{
		
	}
}
