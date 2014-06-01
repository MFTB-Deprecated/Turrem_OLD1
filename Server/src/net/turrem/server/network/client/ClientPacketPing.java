package net.turrem.server.network.client;

import java.io.DataInput;
import java.io.IOException;

import net.turrem.server.network.server.ServerPacketPing;
import net.turrem.server.world.ClientPlayer;

public class ClientPacketPing extends ClientPacket
{
	public long readTime;
	public long clientSendTime;
	
	public ClientPacketPing(String user, DataInput data) throws IOException
	{
		super(user);
		this.readTime = System.nanoTime();
		this.clientSendTime = data.readLong();
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
