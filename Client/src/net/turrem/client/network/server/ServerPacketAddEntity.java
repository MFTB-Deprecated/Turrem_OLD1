package net.turrem.client.network.server;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

import net.turrem.client.game.entity.ClientEntity;
import net.turrem.client.game.entity.EntityRegistry;
import net.turrem.client.game.world.ClientWorld;

public class ServerPacketAddEntity extends ServerPacket
{
	public int entityId;
	public String entityType;
	public float x;
	public float y;
	public float z;
	public byte[] extra;

	private ServerPacketAddEntity(DataInput data, int length, byte type) throws IOException
	{	
		super(type);
		length -= 4;
		this.entityId = data.readInt();
		
		length -= 1;
		int stringlength = data.readByte() & 0xFF;
		
		length -= stringlength;
		byte[] stringbytes = new byte[stringlength];
		data.readFully(stringbytes);
		this.entityType = new String(stringbytes, "UTF-8");
		
		length -= 4;
		this.x = data.readFloat();
		length -= 4;
		this.y = data.readFloat();
		length -= 4;
		this.z = data.readFloat();
		
		this.extra = new byte[length];
		data.readFully(this.extra);
	}
	
	public static ServerPacketAddEntity create(DataInput data, int length, byte type) throws IOException
	{	
		return new ServerPacketAddEntity(data, length, type);
	}

	public ClientEntity makeEntity(ClientWorld world)
	{
		DataInput edat = new DataInputStream(new ByteArrayInputStream(this.extra));
		return EntityRegistry.newInstance(this.entityType, world, this.entityId, this.x, this.y, this.z, edat);
	}
}
