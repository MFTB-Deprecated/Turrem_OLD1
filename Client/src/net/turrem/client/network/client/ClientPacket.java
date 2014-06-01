package net.turrem.client.network.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class ClientPacket
{
	public void write(DataOutputStream stream) throws IOException
	{
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		this.writePacket(new DataOutputStream(data));
		stream.writeByte(this.type());
		if (data.size() <= 0xFFFFFFFF)
		{
			stream.writeShort(data.size());
		}
		else
		{
			stream.writeShort(0xFFFFFFFF);
			stream.writeInt(data.size());
		}
		data.writeTo(stream);
	}
	
	protected abstract void writePacket(DataOutput stream) throws IOException;

	public abstract byte type();
}
