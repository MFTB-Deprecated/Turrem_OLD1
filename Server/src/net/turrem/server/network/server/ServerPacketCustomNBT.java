package net.turrem.server.network.server;

import java.io.DataOutput;
import java.io.IOException;

import net.turrem.utils.nbt.NBTCompound;

public class ServerPacketCustomNBT extends ServerPacket
{
	public String customType;
	public NBTCompound data;
	
	@Override
	protected void writePacket(DataOutput stream) throws IOException
	{
		byte[] typebytes = this.customType.getBytes("UTF-8");
		stream.writeByte(typebytes.length);
		stream.write(typebytes);
		data.writeAsRoot(stream);
	}

	@Override
	public byte type()
	{
		return (byte) 0xFE;
	}
}
