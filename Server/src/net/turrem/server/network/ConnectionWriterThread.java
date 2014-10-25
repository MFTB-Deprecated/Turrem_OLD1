package net.turrem.server.network;

import net.turrem.server.Config;

public class ConnectionWriterThread extends Thread
{
	final GameConnection theConnection;
	
	public ConnectionWriterThread(GameConnection connection)
	{
		super("\"" + connection.name + "\" writer thread");
		this.theConnection = connection;
	}
	
	@Override
	public void run()
	{
		while (this.theConnection.isRunning())
		{
			if (!GameConnection.writePacket(this.theConnection))
			{
				this.theConnection.flushWrite();
				try
				{
					sleep(Config.connectionWriteSleep);
				}
				catch (InterruptedException e)
				{
					
				}
			}
		}
	}
}
