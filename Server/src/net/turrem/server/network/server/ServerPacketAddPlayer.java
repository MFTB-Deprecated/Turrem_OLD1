package net.turrem.server.network.server;

import java.io.DataOutput;
import java.io.IOException;

public class ServerPacketAddPlayer extends ServerPacket
{
	public short playerID;
	public short teamId;
	public String playerName;
	
	@Override
	protected void writePacket(DataOutput stream) throws IOException
	{
		stream.writeShort(this.playerID);
		stream.writeShort(this.teamId);
		byte[] namebytes = this.playerName.getBytes("UTF-8");
		stream.write(namebytes);
	}

	@Override
	public byte type()
	{
		return (byte) 0x22;
	}
}
