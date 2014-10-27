package net.turrem.app.client.network.server;

import java.io.DataInput;
import java.io.IOException;

public class ServerPacketCustom extends ServerPacket
{
	public String customType;
	public byte[] packet;
	
	private ServerPacketCustom(DataInput data, int length, byte type) throws IOException
	{
		super(type);
		length -= 1;
		int stringlength = data.readByte() & 0xFF;
		
		length -= stringlength;
		byte[] stringbytes = new byte[stringlength];
		data.readFully(stringbytes);
		this.customType = new String(stringbytes, "UTF-8");
		
		this.packet = new byte[length];
		data.readFully(this.packet);
	}
	
	public static ServerPacketCustom create(DataInput data, int length, byte type) throws IOException
	{
		return new ServerPacketCustom(data, length, type);
	}
}
