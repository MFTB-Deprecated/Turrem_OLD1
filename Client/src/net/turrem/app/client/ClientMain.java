package net.turrem.app.client;

public class ClientMain
{
	public static void main(String[] args)
	{
		String dir = System.getProperty("user.dir");
		dir = dir.replaceAll("\\\\", "\\/");
		dir += "/";
		String user = null;
		if (args.length >= 1)
		{
			user = args[0];
		}
		if (args.length >= 2)
		{
			Turrem.networkLoc = args[1];
		}
		Session session = new Session(user);
		Turrem turrem = new Turrem(session, dir);
		Thread.currentThread().setName("Turrem Client");
		turrem.run();
	}
}
