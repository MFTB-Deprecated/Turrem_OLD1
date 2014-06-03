package net.turrem.client.network.server;

import java.io.DataInput;
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
	
	public ServerPacketChat(DataInput data, int length) throws IOException
	{		
		length -= 1;
		byte tord = data.readByte();
		this.type = ChatType.values()[tord & 0xFF];
		
		length -= 1;
		int fromlength = data.readByte() & 0xFF;
		
		length -= fromlength;
		byte[] frombytes = new byte[fromlength];
		data.readFully(frombytes);
		this.from = new String(frombytes, "UTF-8");
		
		byte[] chatbytes = new byte[length];
		data.readFully(chatbytes);
		this.chat = new String(chatbytes, "UTF-8");
	}
}
