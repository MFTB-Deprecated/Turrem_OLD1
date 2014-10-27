package net.turrem.app.server.network.server;

import java.io.DataOutput;
import java.io.IOException;

import net.turrem.blueutils.nbt.NBTCompound;

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
		this.data.writeAsRoot(stream);
	}
	
	@Override
	public byte type()
	{
		return (byte) 0xFE;
	}
}
