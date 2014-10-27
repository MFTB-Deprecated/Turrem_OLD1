package net.turrem.app.client.network.server;

import java.io.DataInput;
import java.io.IOException;

public class ServerPacketAddPlayer extends ServerPacket
{
	public short playerID;
	public short teamId;
	public String playerName;
	
	private ServerPacketAddPlayer(DataInput data, int length, byte type) throws IOException
	{
		super(type);
		this.playerID = data.readShort();
		length -= 2;
		
		this.teamId = data.readShort();
		length -= 2;
		
		byte[] stringbytes = new byte[length];
		data.readFully(stringbytes);
		this.playerName = new String(stringbytes, "UTF-8");
	}
	
	public static ServerPacketAddPlayer create(DataInput data, int length, byte type) throws IOException
	{
		return new ServerPacketAddPlayer(data, length, type);
	}
}
