package net.turrem.app.client.network.client;

import java.io.DataOutput;
import java.io.IOException;

public class ClientPacketCustom extends ClientPacket
{
	public String customType;
	public byte[] packet;
	
	@Override
	protected void writePacket(DataOutput stream) throws IOException
	{
		byte[] typebytes = this.customType.getBytes("UTF-8");
		stream.writeByte(typebytes.length);
		stream.write(typebytes);
		stream.write(this.packet);
	}
	
	@Override
	public byte type()
	{
		return (byte) 0xFF;
	}
}
