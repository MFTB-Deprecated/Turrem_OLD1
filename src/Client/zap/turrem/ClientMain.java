package zap.turrem;

import zap.turrem.client.Session;
import zap.turrem.client.Turrem;

public class ClientMain
{
	public static void main(String[] args)
	{
		String dir = System.getProperty("user.dir");
		dir = dir.replaceAll("\\\\", "\\/");
		dir += "/";
		Turrem t = new Turrem(dir, new Session());
		Thread.currentThread().setName("Turrem Client");
		t.run();
	}
}
