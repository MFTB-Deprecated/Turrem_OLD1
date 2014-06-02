package net.turrem.server.network.client;

import net.turrem.server.world.ClientPlayer;

/**
 * A packet received server-side and sent from a client
 */
public abstract class ClientPacket
{
	/**
	 * The name of the client the packet was received from
	 */
	public String user;
	private final byte packetType;

	public ClientPacket(String user, byte type)
	{
		this.user = user;
		this.packetType = type;
	}

	public byte getPacketType()
	{
		return this.packetType;
	}

	public void process(ClientPlayer player)
	{
		if (player.reviewPacket(this) && ClientPacketManager.reviewPacket(this, player))
		{
			ClientPacketManager.processPacket(this, player);
			player.processPacket(this);
		}
	}
}