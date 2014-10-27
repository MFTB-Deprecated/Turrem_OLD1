package net.turrem.app.client.network.server;

import net.turrem.app.client.game.world.ClientWorld;

public class ServerPacketKeepAlive extends ServerPacket
{
	private ServerPacketKeepAlive(byte type)
	{
		super(type);
	}
	
	public static ServerPacketKeepAlive create(byte type)
	{
		return new ServerPacketKeepAlive(type);
	}
	
	@Override
	public void processPacket(ClientWorld world)
	{
		
	}
}
