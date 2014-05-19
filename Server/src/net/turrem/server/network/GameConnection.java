package net.turrem.server.network;

import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.turrem.server.network.client.ClientPacket;
import net.turrem.server.network.server.ServerPacket;

public class GameConnection
{
	public String name;
	public Socket network;
	
	public Queue<ServerPacket> outgoing;
	public Queue<ClientPacket> incoming;
	
	public GameConnection(String name, Socket network)
	{
		this.name = name;
		this.network = network;
        this.outgoing = new ConcurrentLinkedQueue<ServerPacket>();
        this.incoming = new ConcurrentLinkedQueue<ClientPacket>();
	}
	
	public ServerPacket getNextToSend()
	{
		ServerPacket pak = null;
		while (!this.outgoing.isEmpty() && pak == null)
		{
			pak = this.outgoing.poll();
			if (!this.isValidToSend(pak))
			{
				pak = null;
			}
		}
		return pak;
	}
	
	public boolean isValidToSend(ServerPacket packet)
	{
		return true;
	}
	
	public ClientPacket getNextToProcess()
	{
		ClientPacket pak = null;
		while (!this.incoming.isEmpty() && pak == null)
		{
			pak = this.incoming.poll();
			if (!this.isValidToProcess(pak))
			{
				pak = null;
			}
		}
		return pak;
	}
	
	public boolean isValidToProcess(ClientPacket packet)
	{
		return true;
	}
}
