package net.turrem.app.client.network;

import net.turrem.app.client.Config;

public class ConnectionReaderThread extends Thread
{
	final GameConnection theConnection;
	
	public ConnectionReaderThread(GameConnection connection)
	{
		super("Network reader thread");
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
