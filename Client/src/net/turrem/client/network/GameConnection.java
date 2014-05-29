package net.turrem.client.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.turrem.client.Config;
import net.turrem.client.game.world.ClientWorld;
import net.turrem.client.network.client.ClientPacket;
import net.turrem.client.network.client.ClientPacketKeepAlive;
import net.turrem.client.network.server.ServerPacket;
import net.turrem.client.network.server.ServerPacketManager;

public class GameConnection
{
	public static int serverLimitPerTick = 1000;
	
	protected ClientWorld theWorld;
	
	public Socket network;

	public Queue<ClientPacket> outgoing;
	public Queue<ServerPacket> incoming;

	protected volatile DataInputStream input;
	protected volatile DataOutputStream output;

	private boolean isRunning = false;

	private int outTimer = 0;
	
	private Thread readThread;
	private Thread writeThread;
	
	private int currentWriteCount = 0;
	
	public GameConnection(Socket network, ClientWorld world) throws IOException
	{
		this.theWorld = world;
		this.isRunning = true;
		this.network = network;
		this.outgoing = new ConcurrentLinkedQueue<ClientPacket>();
		this.incoming = new ConcurrentLinkedQueue<ServerPacket>();

		this.input = new DataInputStream(this.network.getInputStream());
		this.output = new DataOutputStream(this.network.getOutputStream());
		
		this.readThread = new ConnectionReaderThread(this);
		this.writeThread = new ConnectionWriterThread(this);
		this.readThread.start();
		this.writeThread.start();
	}
	
	public void addToSendQueue(ClientPacket packet)
	{
		if (this.isRunning)
		{
			this.outgoing.add(packet);
			if (this.sendQueueSize() > Config.connectionOutQueueOverflow)
			{
				this.shutdown("Out queue overflow");
			}
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
		ServerPacket pak;
		try
		{
			pak = ServerPacketManager.readSinglePacket(this.input);
		}
		catch (IOException e)
		{
			this.shutdown("IOException during read", e);
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
		ClientPacket pak = this.outgoing.poll();
		if (pak != null)
		{
			try
			{
				pak.write(this.output);
				this.currentWriteCount++;
				return true;
			}
			catch (IOException e)
			{
				this.shutdown("IOException during write", e);
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

	public void flushWrite()
	{
		if (this.isRunning)
		{
			if (this.currentWriteCount == 0)
			{
				ClientPacketKeepAlive alive = new ClientPacketKeepAlive();
				try
				{
					alive.write(this.output);
				}
				catch (IOException e)
				{
					this.shutdown("Failed write keep alive", e);
				}
			}
			this.currentWriteCount = 0;
			try
			{
				this.output.flush();
			}
			catch (IOException e)
			{
				this.shutdown("Failed to flush write", e);
			}
		}
	}

	public void processPackets()
	{
		if (this.incoming.isEmpty())
		{
			if (this.outTimer++ > Config.connectionTimeoutLimit)
			{
				this.shutdown("Timeout");
			}
		}
		else
		{
			this.outTimer = 0;
		}
		if (this.recieveQueueSize() > Config.connectionInQueueOverflow)
		{
			this.shutdown("In queue overflow");
		}
		int i = serverLimitPerTick;
		while (i-- > 0 && this.isRunning)
		{
			ServerPacket pak = this.incoming.poll();
			if (pak != null)
			{
				this.theWorld.processPacket(pak);
			}
			else
			{
				break;
			}
		}
	}

	public void shutdown(String reason, Exception... cause)
	{
		System.out.println("Network Shutdown: " + reason);
		if (this.isRunning)
		{
			this.isRunning = false;

			try
			{
				this.network.shutdownInput();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			try
			{
				this.network.shutdownOutput();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			try
			{
				this.network.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			this.input = null;
			this.output = null;
			this.network = null;
		}
	}
}
