package net.turrem.client.network.server;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ServerPacketManager
{
	public static ServerPacket readSinglePacket(InputStream stream) throws IOException
	{
		byte type = (byte) stream.read();
		int length = (stream.read() << 8) | (stream.read() << 0);
		if (length == 0xFFFFFFFF)
		{
			length = (stream.read() << 24) | (stream.read() << 16) | (stream.read() << 8) | (stream.read() << 0);
		}
		byte[] packet = new byte[length];
		stream.read(packet);
		DataInput input = new DataInputStream(new ByteArrayInputStream(packet));
		return readPacket(type, length, input);
	}
	
	public static ServerPacket readPacket(byte packetType, int length, DataInput data) throws IOException
	{
		switch (packetType & 0xFF)
		{
			case 0x20:
				return new ServerPacketTerrain(data);
			case 0x21:
				return new ServerPacketMaterialSync(data);
			case 0x22:
				return new ServerPacketAddPlayer(data, length);
			case 0x90:
				return new ServerPacketAddEntity(data, length);
			case 0xFD:
				return new ServerPacketKeepAlive();
			case 0xFE:
				return new ServerPacketCustomNBT(data, length);
			case 0xFF:
				return new ServerPacketCustom(data, length);
			default:
				return null;
		}
	}
}
