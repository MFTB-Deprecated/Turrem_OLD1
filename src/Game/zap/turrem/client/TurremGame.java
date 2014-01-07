package zap.turrem.client;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import zap.turrem.Turrem;
import zap.turrem.loaders.java.JarFileLoader;
import zap.turrem.tech.branch.BranchList;
import zap.turrem.tech.list.TechList;
import zap.turrem.utils.StopTimer;

public class TurremGame implements ITurremGame
{
	public static final float tps = 40;
	public final int tickspace;

	private long mils;

	public Turrem theTurrem;

	private boolean running;

	private long tickcount = 0;

	public TurremGame(Turrem turrem)
	{
		this.theTurrem = turrem;
		this.tickspace = (int) (1000 / tps);
	}

	@Override
	public void runGameLoop()
	{
		long diff = System.currentTimeMillis() - this.mils;
		if (diff >= this.tickspace || diff < 0)
		{
			this.mils = System.currentTimeMillis();
			this.runTick();
		}
	}

	public void runTick()
	{
		this.tickcount++;

		// TODO Program Game

		if (this.tickcount > 1000)
		{
			this.gotoMainMenu();
		}
	}

	public boolean isrunning()
	{
		return this.running;
	}

	@Override
	public void onStart()
	{
		this.doLoad();
	}

	public void doLoad()
	{
		StopTimer timer = new StopTimer();
		timer.start();
		this.doLoadTech();
		long nano = timer.end();
		System.out.printf("Loaded %d techs in %.2fms%n", TechList.getSize(), nano * 0.000001D);
		
		timer.start();
		this.doLoadTechBranches();
		nano = timer.end();
		System.out.printf("Loaded %d tech branches in %.2fms%n", BranchList.branchCount(), nano * 0.000001D);
	}
	
	public void doLoadTechBranches()
	{
		TechList.loadBranches();
	}
	
	public void doLoadTech()
	{

		File thejar = new File(this.theTurrem.getDir() + "jars/" + "tech.jar");
		JarFileLoader techloader = new JarFileLoader(thejar);

		if (!thejar.exists())
		{
			System.out.println("Error! tech.jar does not exist!");
			return;
		}

		URL jarfile;

		try
		{
			jarfile = new URL("jar", "", "file:" + thejar.getAbsolutePath() + "!/");
		}
		catch (MalformedURLException urle)
		{
			urle.printStackTrace();
			return;
		}

		URLClassLoader cl = URLClassLoader.newInstance(new URL[] { jarfile });

		String[] classlist = null;

		try
		{
			classlist = techloader.getClassList();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}

		for (int i = 0; i < classlist.length; i++)
		{
			Class<?> stone = null;

			try
			{
				stone = cl.loadClass(classlist[i]);
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			
			if (stone != null)
			{
				TechList.loadTechClass(stone);
			}
		}
	}

	public void gotoMainMenu()
	{
		this.theTurrem.gotoMenu();
		this.running = false;
	}

	@Override
	public void run()
	{
		this.running = true;

		try
		{
			this.onStart();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			this.gotoMainMenu();
			return;
		}

		try
		{
			while (this.running)
			{
				if (this.running)
				{
					try
					{
						this.runGameLoop();
					}
					catch (Throwable e)
					{
						e.printStackTrace();
						this.gotoMainMenu();
						return;
					}

					continue;
				}
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			this.gotoMainMenu();
			return;
		}

		this.running = true;
	}
}
