package net.turrem.server.network.client;

/**
 * Requests any available info on the server (difficulty, tick rate, version,
 * etc). Can also be done through ClientPacketRequest
 */
public class ClientPacketServerInfoRequest extends ClientPacket
{
	private ClientPacketServerInfoRequest(String user, byte type)
	{
		super(user, type);
	}
	
	public static ClientPacketServerInfoRequest create(String user, byte type)
	{
		return new ClientPacketServerInfoRequest(user, type);
	}
}
