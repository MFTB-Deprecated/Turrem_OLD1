package net.turrem.client.network.server;

import java.io.DataInput;
import java.io.IOException;

import net.turrem.client.game.entity.ClientEntity;
import net.turrem.client.game.entity.EntityRegistry;
import net.turrem.client.game.world.ClientWorld;
import net.turrem.utils.nbt.NBTCompound;

public class ServerPacketAddEntity extends ServerPacket
{
	public long entityId;
	public String entityType;
	public float x;
	public float y;
	public float z;
	public NBTCompound data;

	private ServerPacketAddEntity(DataInput data, int length, byte type) throws IOException
	{	
		super(type);
		length -= 8;
		this.entityId = data.readLong();
		
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
		
		this.data = NBTCompound.readAsRoot(data);
	}
	
	public static ServerPacketAddEntity create(DataInput data, int length, byte type) throws IOException
	{	
		return new ServerPacketAddEntity(data, length, type);
	}

	public ClientEntity makeEntity(ClientWorld world)
	{
		return EntityRegistry.newInstance(this.entityType, world, this.entityId, this.x, this.y, this.z, this.data);
	}
}
