package net.turrem.app.server.network.server;

import java.io.DataOutput;
import java.io.IOException;

public class ServerPacketChat extends ServerPacket
{
	public static enum ChatType
	{
		PLAYER,
		SERVER;
	}
	
	public ChatType type;
	public String from;
	public String chat;
	
	@Override
	protected void writePacket(DataOutput stream) throws IOException
	{
		stream.writeByte(this.type.ordinal());
		byte[] frombytes = this.from.getBytes("UTF-8");
		stream.writeByte(frombytes.length);
		stream.write(frombytes);
		byte[] chatbytes = this.chat.getBytes("UTF-8");
		stream.write(chatbytes);
	}
	
	@Override
	public byte type()
	{
		return (byte) 0xA0;
	}
}
