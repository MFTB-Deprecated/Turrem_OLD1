package net.turrem.server.network.client;

import java.io.DataInput;
import java.io.IOException;

public class ClientPacketAction extends ClientPacket
{
	public String user;
	public int[] entities;
	public String action;
	public byte[] actionData;
	
	public ClientPacketAction(DataInput data, String user) throws IOException
	{
		this.user = user;
		this.entities = new int[data.readByte() & 0xFF];
		for (int i = 0; i < this.entities.length; i++)
		{
			this.entities[i] = data.readInt();
		}
		this.action = data.readUTF();
		this.actionData = new byte[data.readShort()];
		data.readFully(this.actionData);
	}
}
