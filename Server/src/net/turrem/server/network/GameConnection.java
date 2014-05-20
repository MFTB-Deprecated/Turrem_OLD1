package net.turrem.server.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.turrem.server.network.client.ClientPacket;
import net.turrem.server.network.client.ClientPacketManager;
import net.turrem.server.network.server.ServerPacket;

public class GameConnection
{
	public String name;
	public Socket network;

	protected NetworkRoom theRoom;

	public Queue<ServerPacket> outgoing;
	public Queue<ClientPacket> incoming;

	protected volatile DataInputStream input;
	protected volatile DataOutputStream output;

	private boolean isRunning = false;

	public GameConnection(String name, Socket network, NetworkRoom room) throws IOException
	{
		this.isRunning = true;
		this.theRoom = room;
		this.name = name;
		this.network = network;
		this.outgoing = new ConcurrentLinkedQueue<ServerPacket>();
		this.incoming = new ConcurrentLinkedQueue<ClientPacket>();

		this.input = new DataInputStream(this.network.getInputStream());
		this.output = new DataOutputStream(this.network.getOutputStream());
	}

	public void addToSendQueue(ServerPacket packet)
	{
		if (this.isRunning)
		{
			this.outgoing.add(packet);
		}
	}

	public int sendQueueSize()
	{
		return this.outgoing.size();
	}

	public int recieveQueueSize()
	{
		return this.incoming.size();
	}

	private boolean readPacket()
	{
		if (!this.isRunning)
		{
			return false;
		}
		ClientPacket pak;
		try
		{
			pak = ClientPacketManager.readSinglePacket(this.name, this.input);
		}
		catch (IOException e)
		{
			return false;
		}
		this.incoming.add(pak);
		return true;
	}

	public static boolean readPacket(GameConnection connection)
	{
		return connection.readPacket();
	}

	private boolean writePacket()
	{
		if (!this.isRunning)
		{
			return false;
		}
		ServerPacket pak = this.outgoing.poll();
		if (pak != null)
		{
			try
			{
				pak.write(this.output);
				return true;
			}
			catch (IOException e)
			{
				return false;
			}
		}
		return false;
	}

	public static boolean writePacket(GameConnection connection)
	{
		return connection.writePacket();
	}
	
	public boolean isRunning()
	{
		return isRunning;
	}
	
	public void processPackets(int maxnum)
	{
		int i = maxnum;
		while(i-- > 0)
		{
			ClientPacket pak = this.incoming.poll();
			if (pak != null)
			{
				
			}
			else
			{
				break;
			}
		}
	}
}
