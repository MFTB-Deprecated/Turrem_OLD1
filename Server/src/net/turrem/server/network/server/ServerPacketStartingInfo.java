package net.turrem.server.network.server;

import java.io.DataOutput;
import java.io.IOException;

import net.turrem.server.Realm;

public class ServerPacketStartingInfo extends ServerPacket
{
	public Realm realm;
	
	public ServerPacketStartingInfo(Realm realm)
	{
		this.realm = realm;
	}
	
	@Override
	protected void writePacket(DataOutput stream) throws IOException
	{
		stream.writeInt((int) realm.startingLocation.xCoord);
		stream.writeInt((int) realm.startingLocation.zCoord);
	}

	@Override
	public byte type()
	{
		return (byte) 0x23;
	}
}
