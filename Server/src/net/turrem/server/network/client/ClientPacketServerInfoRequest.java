package net.turrem.server.network.client;

/**
 * Requests any available info on the server (difficulty, tick rate, version, etc). Can also be done through ClientPacketRequest
 */
public class ClientPacketServerInfoRequest extends ClientPacket
{
	public ClientPacketServerInfoRequest(String user)
	{
		super(user);
	}
}
