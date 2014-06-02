package net.turrem.server.network.client;

public class ClientPacketKeepAlive extends ClientPacket
{
	private ClientPacketKeepAlive(String user, byte type)
	{
		super(user, type);
	}
	
	public static ClientPacketKeepAlive create(String user, byte type)
	{
		return new ClientPacketKeepAlive(user, type);
	}
}
