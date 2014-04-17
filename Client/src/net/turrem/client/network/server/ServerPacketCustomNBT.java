package net.turrem.client.network.server;

import java.io.DataInput;
import java.io.IOException;

import net.turrem.utils.nbt.NBTCompound;

public class ServerPacketCustomNBT extends ServerPacket
{
	public String customType;
	public NBTCompound nbt;
	
	public ServerPacketCustomNBT(DataInput data, int length) throws IOException
	{		
		length -= 1;
		int stringlength = data.readByte() & 0xFF;
		
		length -= stringlength;
		byte[] stringbytes = new byte[stringlength];
		data.readFully(stringbytes);
		this.customType = new String(stringbytes, "UTF-8");
		
		this.nbt = NBTCompound.readAsRoot(data);
	}
}
