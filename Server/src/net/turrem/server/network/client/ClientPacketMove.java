package net.turrem.server.network.client;

import java.io.DataInput;
import java.io.IOException;

/**
 * Requests the movement of a unit or group of units. Can also be done through ClientPacketAction
 */
public class ClientPacketMove extends ClientPacket
{
	/**
	 * The entities that the player wants to move (identification numbers)
	 */
	public long[] entities;
	/**
	 * X coordinate
	 */
	public float xpos;
	/**
	 * Z coordinate
	 */
	public float zpos;
	
	private ClientPacketMove(DataInput data, String user, byte type) throws IOException
	{
		super(user, type);
		this.entities = new long[data.readByte() & 0xFF];
		for (int i = 0; i < this.entities.length; i++)
		{
			this.entities[i] = data.readLong();
		}
		this.xpos = data.readFloat();
		this.zpos = data.readFloat();
	}
	
	public static ClientPacketMove create(DataInput data, String user, byte type) throws IOException
	{
		return new ClientPacketMove(data, user, type);
	}
}
