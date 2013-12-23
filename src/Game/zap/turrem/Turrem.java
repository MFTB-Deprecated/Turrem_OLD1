package zap.turrem;

import zap.turrem.tech.TechList;
import zap.turrem.utils.Timer;

public class Turrem
{
	private Session session;
	private String dir;
	
	private static Turrem instance;

	public static Turrem getTurrem()
	{
		return instance;
	}

	public Turrem(String dir, Session session)
	{
		this.dir = dir;
		this.session = session;
		instance = this;
	}

	public void doLoad()
	{
		Timer timer = new Timer();
		timer.start();
		try
		{
			JarLoader.loadTechJar(this.dir + "jars/");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		TechList.loadBranches();
		long nano = timer.end();
		System.out.printf("Loaded %d techs in %.2fms%n", TechList.techCount(), nano * 0.000001D);
	}

	public Session getSession()
	{
		return session;
	}
	
	public String getDir()
	{
		return dir;
	}
}
