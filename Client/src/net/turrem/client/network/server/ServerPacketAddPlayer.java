package net.turrem.client.network.server;

import java.io.DataInput;
import java.io.IOException;

public class ServerPacketAddPlayer extends ServerPacket
{
	public short playerID;
	public short teamId;
	public String playerName;
	
	public ServerPacketAddPlayer(DataInput data, int length) throws IOException
	{
		this.playerID = data.readShort();
		length -= 2;
		
		this.teamId = data.readShort();
		length -= 2;
		
		byte[] stringbytes = new byte[length];
		data.readFully(stringbytes);
		this.playerName = new String(stringbytes, "UTF-8");
	}
}
