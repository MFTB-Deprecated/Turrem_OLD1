package net.turrem.server.network.client;

import java.io.DataInput;
import java.io.IOException;

import net.turrem.utils.nbt.NBTCompound;

/**
 * Can be used for anything
 */
public class ClientPacketCustomNBT extends ClientPacket
{
	/**
	 * Identifies the type/use of the packet
	 */
	public String customType;
	/**
	 * The custom packet's NBT data
	 */
	public NBTCompound data;

	private ClientPacketCustomNBT(String user, DataInput data, byte type) throws IOException
	{
		super(user, type);
		
		int stringlength = data.readByte() & 0xFF;
		
		byte[] stringbytes = new byte[stringlength];
		data.readFully(stringbytes);
		this.customType = new String(stringbytes, "UTF-8");
		
		this.data = NBTCompound.readAsRoot(data);
	}
	
	public static ClientPacketCustomNBT create(String user, DataInput data, byte type) throws IOException
	{
		return new ClientPacketCustomNBT(user, data, type);
	}
}
