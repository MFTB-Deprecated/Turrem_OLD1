package net.turrem.server.network.client;

import java.io.DataInput;
import java.io.IOException;

/**
 * Reports any actions the player performs on entities (buildings/units/etc)
 */
public class ClientPacketAction extends ClientPacket
{
	/**
	 * The entities that the action was performed on (identification numbers)
	 */
	public int[] entities;
	/**
	 * A string identifying the type of action
	 */
	public String action;
	/**
	 * Any additional data relating to the action (locations, targets, etc.)
	 */
	public byte[] actionData;
	
	public ClientPacketAction(DataInput data, String user, int length) throws IOException
	{
		super(user);
		length -= 1;
		this.entities = new int[data.readByte() & 0xFF];
		for (int i = 0; i < this.entities.length; i++)
		{
			length -= 4;
			this.entities[i] = data.readInt();
		}
		
		length -= 1;
		int stringlength = data.readByte() & 0xFF;
		
		length -= stringlength;
		byte[] stringbytes = new byte[stringlength];
		data.readFully(stringbytes);
		this.action = new String(stringbytes, "UTF-8");
		
		this.actionData = new byte[length];
		data.readFully(this.actionData);
	}
}
