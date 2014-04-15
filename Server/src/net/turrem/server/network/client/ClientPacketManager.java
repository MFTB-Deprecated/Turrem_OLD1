package net.turrem.server.network.client;

import java.io.DataInput;
import java.io.IOException;

public class ClientPacketManager
{
	public ClientPacket readPacket(byte packetType, int length, DataInput data, String user) throws IOException
	{
		switch (packetType)
		{
			case 0:
				return new ClientPacketAction(data, user);
			case 1:
				return new ClientPacketMove(data, user);
			default:
				return null;
		}
	}
}
