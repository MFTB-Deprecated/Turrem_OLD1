package net.turrem.client.network.server;

import java.io.DataInput;
import java.io.IOException;

public class ServerPacketMoveEntity extends ServerPacket
{
	public long entityId;
	public float xpos;
	public float ypos;
	public float zpos;
	public short moveTime = 0;
	
	private ServerPacketMoveEntity(DataInput data, int length, byte type) throws IOException
	{
		super(type);
		length -= 8;
		this.entityId = data.readLong();
		length -= 4;
		this.xpos = data.readFloat();
		length -= 4;
		this.ypos = data.readFloat();
		length -= 4;
		this.zpos = data.readFloat();
		if (length == 2)
		{
			this.moveTime = data.readShort();
		}
	}
	
	public static ServerPacketMoveEntity create(DataInput data, int length, byte type) throws IOException
	{
		return new ServerPacketMoveEntity(data, length, type);
	}
}
