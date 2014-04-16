package net.turrem.server.network.client;

public abstract class ClientPacket
{
	/**
	 * The name of the client the packet was received from
	 */
	public String user;
	
	public ClientPacket(String user)
	{
		this.user = user;
	}
}