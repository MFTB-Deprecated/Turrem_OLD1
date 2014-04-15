package net.turrem.server.network.client;

import java.io.DataInput;
import java.io.IOException;

public class ClientPacketMove extends ClientPacket
{
	public String user;
	public int[] entities;
	public int xpos;
	public int ypos;
	public int zpos;
	
	public ClientPacketMove(DataInput data, String user) throws IOException
	{
		this.user = user;
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
