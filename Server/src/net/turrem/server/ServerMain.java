package net.turrem.server;

public class ServerMain
{
	public static void main(String[] args)
	{
		String dir = System.getProperty("user.dir");
		dir = dir.replaceAll("\\\\", "\\/");
		dir += "/";
		String save = dir + "defaultsave/";
		if (args.length >= 1)
		{
			save = args[0];
		}
		if (args.length >= 2)
		{
			TurremServer.networkLoc = args[1];
		}
		save = save.replaceAll("\\\\", "\\/");
		TurremServer turrem = new TurremServer(dir, save);
		Thread.currentThread().setName("Turrem Server");
		turrem.run();
	}
}
