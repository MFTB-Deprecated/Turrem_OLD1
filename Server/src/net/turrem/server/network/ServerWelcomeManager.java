package net.turrem.server.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import net.turrem.server.Config;
import net.turrem.server.TurremServer;

public class ServerWelcomeManager extends Thread
{
	protected TurremServer theTurrem;
	protected NetworkRoom room;
	
	public ServerWelcomeManager(TurremServer turrem, NetworkRoom room)
	{
		this.setName("Client Welcomer");
		this.theTurrem = turrem;
		this.room = room;
	}
	
	@Override
	public void run()
	{
		ServerSocket listener;
		try
		{
			listener = new ServerSocket(Config.turremServerPort);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		while (!listener.isClosed() && this.theTurrem.acceptingClients())
		{
			try
			{
				this.doListen(listener.accept());
			}
			catch (IOException e)
			{
				System.out.println("Welcome Failed");
				e.printStackTrace();
			}
		}
		try
		{
			listener.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void doListen(Socket socket) throws IOException
	{
		DataInputStream in = new DataInputStream(socket.getInputStream());
		String name = in.readUTF();
		GameConnection client = new GameConnection(name, socket, this.room);
		if(!this.room.addClient(client))
		{
			socket.close();
		}
	}
}
