package zap.turrem;

public class Main
{
	public static void main(String[] args)
	{
		String dir = System.getProperty("user.dir");
		dir = dir.replaceAll("\\\\", "\\/");
		dir += "/";
		Turrem t = new Turrem(dir, new Session());
		Thread.currentThread().setName("Turrem main thread");
		t.run();
	}
}
