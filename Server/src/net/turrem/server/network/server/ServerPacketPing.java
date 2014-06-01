package net.turrem.server.network.server;

import java.io.DataOutput;
import java.io.IOException;

public class ServerPacketPing extends ServerPacket
{
	public long readTime;
	public long processTime;
	public long clientSendTime;
	
	@Override
	protected void writePacket(DataOutput stream) throws IOException
	{
		stream.writeInt((int) (this.processTime - this.readTime));
		stream.writeInt((int) (System.nanoTime() - this.processTime));
		stream.writeLong(this.clientSendTime);
	}
	
	@Override
	public byte type()
	{
		return (byte) 0x32;
	}
}
