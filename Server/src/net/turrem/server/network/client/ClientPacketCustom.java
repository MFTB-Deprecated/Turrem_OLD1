package net.turrem.server.network.client;

import java.io.DataInput;
import java.io.IOException;

/**
 * Can be used for anything
 */
public class ClientPacketCustom extends ClientPacket
{
	/**
	 * Identifies the type/use of the packet
	 */
	public String customType;
	/**
	 * The custom packet's data
	 */
	public byte[] packet;

	private ClientPacketCustom(String user, DataInput data, int length, byte type) throws IOException
	{
		super(user, type);
		
		length -= 1;
		int stringlength = data.readByte() & 0xFF;
		
		length -= stringlength;
		byte[] stringbytes = new byte[stringlength];
		data.readFully(stringbytes);
		this.customType = new String(stringbytes, "UTF-8");
		
		this.packet = new byte[length];
		data.readFully(this.packet);
	}
	
	public static ClientPacketCustom create(String user, DataInput data, int length, byte type) throws IOException
	{
		return new ClientPacketCustom(user, data, length, type);
	}
}
