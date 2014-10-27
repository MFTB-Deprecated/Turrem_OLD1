package net.turrem.app.client.network.server;

import java.io.DataInput;
import java.io.IOException;

public class ServerPacketMaterialSync extends ServerPacket
{
	public short num;
	public String name;
	public byte r;
	public byte g;
	public byte b;
	
	private ServerPacketMaterialSync(DataInput data, byte type) throws IOException
	{
		super(type);
		this.num = data.readShort();
		int stringlength = data.readByte() & 0xFF;
		byte[] stringbytes = new byte[stringlength];
		data.readFully(stringbytes);
		this.name = new String(stringbytes, "UTF-8");
		this.r = data.readByte();
		this.g = data.readByte();
		this.b = data.readByte();
	}
	
	public static ServerPacketMaterialSync create(DataInput data, byte type) throws IOException
	{
		return new ServerPacketMaterialSync(data, type);
	}
}
