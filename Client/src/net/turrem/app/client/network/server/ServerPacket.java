package net.turrem.app.client.network.server;

import net.turrem.app.client.game.world.ClientWorld;

public abstract class ServerPacket
{
	private final byte packetType;
	
	public ServerPacket(byte type)
	{
		this.packetType = type;
	}
	
	public byte getPacketType()
	{
		return this.packetType;
	}
	
	public void processPacket(ClientWorld world)
	{
		world.processPacket(this);
		ServerPacketManager.processPacket(this, world);
	}
}
