package net.turrem.server.network.client;

import java.io.DataInput;
import java.io.IOException;

/**
 * Contains a chat string to be parsed server-side and distributed/executed
 */
public class ClientPacketChat extends ClientPacket
{
	/**
	 * The string sent
	 */
	public String chat;
	
	private ClientPacketChat(String user, DataInput data, int length, byte type) throws IOException
	{
		super(user, type);
		
		byte[] stringbytes = new byte[length];
		data.readFully(stringbytes);
		this.chat = new String(stringbytes, "UTF-8");
	}
	
	public static ClientPacketChat create(String user, DataInput data, int length, byte type) throws IOException
	{
		return new ClientPacketChat(user, data, length, type);
	}
}
