package net.turrem.app.client;

public class Session
{
	public final String username;
	private final long startTime;
	
	public Session(String user)
	{
		this.username = user;
		this.startTime = System.currentTimeMillis();
	}
	
	public final long getDuration()
	{
		return System.currentTimeMillis() - this.startTime;
	}
}
