package net.turrem.app.server.network;

import net.turrem.app.server.Config;

public class ConnectionReaderThread extends Thread
{
	final GameConnection theConnection;
	
	public ConnectionReaderThread(GameConnection connection)
	{
		super("\"" + connection.name + "\" reader thread");
		this.theConnection = connection;
	}
	
	@Override
	public void run()
	{
		while (this.theConnection.isRunning())
		{
			if (!GameConnection.readPacket(this.theConnection))
			{
				try
				{
					sleep(Config.connectionReadSleep);
				}
				catch (InterruptedException e)
				{
					
				}
			}
		}
	}
}
