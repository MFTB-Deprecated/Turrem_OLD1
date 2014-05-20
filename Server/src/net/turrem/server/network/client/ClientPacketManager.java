package net.turrem.server.network.client;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClientPacketManager
{
	public static ClientPacket readSinglePacket(String user, InputStream stream) throws IOException
	{
		byte type = (byte) stream.read();
		int length = (stream.read() << 8) | (stream.read() << 0);
		byte[] packet = new byte[length];
		stream.read(packet);
		DataInput input = new DataInputStream(new ByteArrayInputStream(packet));
		return readPacket(type, length, input, user);
	}
	
	public static ClientPacket readPacket(byte packetType, int length, DataInput data, String user) throws IOException
	{
		switch (packetType & 0xFF)
		{
			case 0x10:
				return new ClientPacketAction(data, user, length);
			case 0x11:
				return new ClientPacketMove(data, user);
			case 0x30:
				return new ClientPacketRequest(user, data, length);
			case 0x31:
				return new ClientPacketServerInfoRequest(user);
			case 0xA0:
				return new ClientPacketChat(user, data, length);
			case 0xFE:
				return new ClientPacketCustomNBT(user, data);
			case 0xFF:
				return new ClientPacketCustom(user, data, length);
			default:
				return null;
		}
	}
}
