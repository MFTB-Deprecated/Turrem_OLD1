package zap.turrem;

import zap.turrem.server.TurremServer;

public class ServerMain
{
	public static void main(String[] args)
	{
		String dir = System.getProperty("user.dir");
		dir = dir.replaceAll("\\\\", "\\/");
		dir += "/";
		TurremServer t = new TurremServer(dir);
		Thread.currentThread().setName("Turrem Server");
		t.run();
	}
}