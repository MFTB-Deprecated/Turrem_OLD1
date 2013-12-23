package zap.turrem;

import zap.turrem.utils.StopTimer;

public class Main
{
	public static void main(String[] args)
	{
		String dir = System.getProperty("user.dir");
		dir = dir.replaceAll("\\\\", "\\/");
		dir += "/";
		Turrem t = new Turrem(dir, new Session());
		Thread.currentThread().setName("Turrem main thread");
		StopTimer timer = new StopTimer();
		timer.start();
		t.run();
		long nano = timer.end();
		System.out.printf("Ran for %.2fs%n", nano * 0.000000001D);
	}
}
