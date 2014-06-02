package net.turrem.server.network.client;

import java.io.DataInput;
import java.io.IOException;

import net.turrem.server.network.server.ServerPacketPing;
import net.turrem.server.world.ClientPlayer;

public class ClientPacketPing extends ClientPacket
{
	public long readTime;
	public long clientSendTime;
	
	private ClientPacketPing(String user, DataInput data, byte type) throws IOException
	{
		super(user, type);
		this.readTime = System.nanoTime();
		this.clientSendTime = data.readLong();
	}
	
	public static ClientPacketPing create(String user, DataInput data, byte type) throws IOException
	{
		return new ClientPacketPing(user, data, type);
	}

	@Override
	public void process(ClientPlayer player)
	{
		ServerPacketPing ping = new ServerPacketPing();
		ping.clientSendTime = this.clientSendTime;
		ping.processTime = System.nanoTime();
		ping.readTime = this.readTime;
		player.theConnection.addToSendQueue(ping);
	}
}
