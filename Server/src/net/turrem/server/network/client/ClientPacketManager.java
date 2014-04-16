package net.turrem.server.network.client;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClientPacketManager
{
	public ClientPacket readSinglePacket(String user, InputStream data) throws IOException
	{
		byte type = (byte) data.read();
		int length = (data.read() << 8) | (data.read() << 0);
		byte[] packet = new byte[length];
		data.read(packet);
		DataInput input = new DataInputStream(new ByteArrayInputStream(packet));
		return this.readPacket(type, length, input, user);
	}
	
	public ClientPacket readPacket(byte packetType, int length, DataInput data, String user) throws IOException
	{
		switch (packetType & 0xFF)
		{
			case 0x00:
				return new ClientPacketAction(data, user, length);
			case 0x01:
				return new ClientPacketMove(data, user);
			case 0x02:
				return new ClientPacketRequest(user, data, length);
			case 0x03:
				return new ClientPacketServerInfoRequest(user);
			case 0xFF:
				return new ClientPacketCustom(user, data, length);
			default:
				return null;
		}
	}
}
