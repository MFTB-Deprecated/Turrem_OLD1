package net.turrem.client.network.server;

import java.io.DataInput;
import java.io.IOException;

public class ServerPacketMaterialSync extends ServerPacket
{
	public short num;
	public String id;
	
	public ServerPacketMaterialSync(DataInput data) throws IOException
	{
		this.num = data.readShort();
		int stringlength = data.readByte() & 0xFF;
		byte[] stringbytes = new byte[stringlength];
		data.readFully(stringbytes);
		this.id = new String(stringbytes, "UTF-8");
	}
}
