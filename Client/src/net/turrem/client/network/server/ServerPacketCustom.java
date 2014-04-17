package net.turrem.client.network.server;

import java.io.DataInput;
import java.io.IOException;

public class ServerPacketCustom extends ServerPacket
{
	public String customType;
	public byte[] packet;
	
	public ServerPacketCustom(DataInput data, int length) throws IOException
	{		
		length -= 1;
		int stringlength = data.readByte() & 0xFF;
		
		length -= stringlength;
		byte[] stringbytes = new byte[stringlength];
		data.readFully(stringbytes);
		this.customType = new String(stringbytes, "UTF-8");
		
		this.packet = new byte[length];
		data.readFully(this.packet);
	}
}
