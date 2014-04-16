package net.turrem.server.network.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class ServerPacket
{
	protected abstract void writePacket(DataOutput stream) throws IOException;
	
	public abstract byte type();
	
	public void write(DataOutputStream stream) throws IOException
	{
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		this.writePacket(new DataOutputStream(data));
		stream.writeByte(this.type());
		stream.writeShort(data.size());
		data.writeTo(stream);
	}
}
