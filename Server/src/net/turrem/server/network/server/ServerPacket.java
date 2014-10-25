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
		stream.writeByte(this.type() & 0xFF);
		if (data.size() < 0xFFFF)
		{
			stream.writeShort(data.size());
		}
		else
		{
			stream.writeShort(0xFFFF);
			stream.writeInt(data.size());
		}
		data.writeTo(stream);
	}
}
