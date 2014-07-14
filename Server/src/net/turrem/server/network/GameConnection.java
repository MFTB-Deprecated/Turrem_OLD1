package net.turrem.server.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.turrem.server.Config;
import net.turrem.server.network.client.ClientPacket;
import net.turrem.server.network.client.ClientPacketManager;
import net.turrem.server.network.server.ServerPacket;
import net.turrem.server.network.server.ServerPacketKeepAlive;
import net.turrem.server.world.ClientPlayer;

/**
 * A server-client connection
 */
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

	private int outTimer = 0;
	
	private Thread readThread;
	private Thread writeThread;
	
	public ClientPlayer player = null;
	
	private int currentWriteCount = 0;

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
		
		this.readThread = new ConnectionReaderThread(this);
		this.writeThread = new ConnectionWriterThread(this);
		this.readThread.start();
		this.writeThread.start();
	}

	/**
	 * Add a packet to the send queue.
	 * @param packet The packet to send to the client
	 */
	public void addToSendQueue(ServerPacket packet)
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
		ClientPacket pak;
		try
		{
			pak = ClientPacketManager.readSinglePacket(this.name, this.input);
		}
		catch (IOException e)
		{
			this.shutdown("IOException during read", e);
			return false;
		}
		if (pak != null)
		{
			this.incoming.add(pak);
		}
		else
		{
			System.err.println("Warning! Null Packet");
		}
		return true;
	}

	/**
	 * Causes the given connection to read a single packet from the TCP stream.
	 * @param connection The client-server connection.
	 * @return True if a packet was successfully read, false otherwise.
	 */
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

	/**
	 * Causes the given connection to write a single packet to the TCP stream.
	 * @param connection The client-server connection.
	 * @return True if a packet was successfully written, false otherwise.
	 */
	public static boolean writePacket(GameConnection connection)
	{
		return connection.writePacket();
	}

	public boolean isRunning()
	{
		return isRunning;
	}

	/**
	 * Should be called after writing each group of packets.
	 */
	public void flushWrite()
	{
		if (this.isRunning)
		{
			if (this.currentWriteCount == 0)
			{
				ServerPacketKeepAlive alive = new ServerPacketKeepAlive();
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

	/**
	 * Causes a number of received packets to be polled from the receive queue and processed by the server.
	 * @param maxnum How many packets to process this call.
	 */
	public void processPackets(int maxnum)
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
		int i = maxnum;
		while (i-- > 0 && this.isRunning)
		{
			ClientPacket pak = this.incoming.poll();
			if (pak != null)
			{
				pak.process(this.player);
			}
			else
			{
				break;
			}
		}
	}
	
	public void onCreate()
	{
		this.theRoom.addPlayerToWorld(this);
		if (this.player != null)
		{
			this.player.joinNetwork(this);
		}
		else
		{
			this.shutdown("Player not assigned");
		}
	}

	/**
	 * Ends this client-server connection.
	 * @param reason A short message describing the reason for the shutdown (ex. "Timeout")
	 * @param cause Any exceptions associated with the shutdown.
	 */
	public void shutdown(String reason, Exception... cause)
	{
		System.out.println("Network Shutdown: " + reason);
		if (this.isRunning)
		{
			if (this.player != null)
			{
				this.player.exit();
			}
			this.isRunning = false;

			try
			{
				this.network.shutdownInput();
			}
			catch (IOException e)
			{
				
			}

			try
			{
				this.network.shutdownOutput();
			}
			catch (IOException e)
			{
				
			}

			try
			{
				this.network.close();
			}
			catch (IOException e)
			{
				
			}

			this.input = null;
			this.output = null;
			this.network = null;
		}
	}
}
