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
	public int[] entities;
	/**
	 * X coordinate
	 */
	public int xpos;
	/**
	 * Y coordinate (should be recalculated before use)
	 */
	public int ypos;
	/**
	 * Z coordinate
	 */
	public int zpos;
	
	public ClientPacketMove(DataInput data, String user) throws IOException
	{
		super(user);
		this.entities = new int[data.readByte() & 0xFF];
		for (int i = 0; i < this.entities.length; i++)
		{
			this.entities[i] = data.readInt();
		}
		this.xpos = data.readInt();
		this.ypos = data.readInt();
		this.zpos = data.readInt();
	}
}
