package net.turrem.client.network.server;

import java.io.DataInput;
import java.io.IOException;

public class ServerPacketMaterialSync extends ServerPacket
{
	public short num;
	public String id;
	
	private ServerPacketMaterialSync(DataInput data, byte type) throws IOException
	{
		super(type);
		this.num = data.readShort();
		int stringlength = data.readByte() & 0xFF;
		byte[] stringbytes = new byte[stringlength];
		data.readFully(stringbytes);
		this.id = new String(stringbytes, "UTF-8");
	}
	
	public static ServerPacketMaterialSync create(DataInput data, byte type) throws IOException
	{
		return new ServerPacketMaterialSync(data, type);
	}
}
