package net.turrem.client.network.server;

import java.io.DataInput;
import java.io.IOException;

import net.turrem.utils.nbt.NBTCompound;

public class ServerPacketCustomNBT extends ServerPacket
{
	public String customType;
	public NBTCompound nbt;
	
	private ServerPacketCustomNBT(DataInput data, int length, byte type) throws IOException
	{		
		super(type);
		length -= 1;
		int stringlength = data.readByte() & 0xFF;
		
		length -= stringlength;
		byte[] stringbytes = new byte[stringlength];
		data.readFully(stringbytes);
		this.customType = new String(stringbytes, "UTF-8");
		
		this.nbt = NBTCompound.readAsRoot(data);
	}
	
	public static ServerPacketCustomNBT create(DataInput data, int length, byte type) throws IOException
	{
		return new ServerPacketCustomNBT(data, length, type);
	}
}
