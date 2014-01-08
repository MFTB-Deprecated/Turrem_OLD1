package zap.turrem.server;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;

import zap.turrem.core.ITurremGame;
import zap.turrem.core.tech.branch.BranchList;
import zap.turrem.core.tech.debug.TechTester;
import zap.turrem.core.tech.item.TechItem;
import zap.turrem.core.tech.list.TechList;
import zap.turrem.loaders.java.JarFileLoader;
import zap.turrem.utils.StopTimer;

public class TurremGame implements ITurremGame
{
	public static int tickSpeed = 20;

	private long mils;

	private boolean running;

	private TechTester techtests = new TechTester();

	public static final boolean testTechs = false;
	
	public TurremServer theTurrem;
	
	public TurremGame(TurremServer server)
	{
		this.theTurrem = server;
	}
	
	public void runGameLoop()
	{
		long diff = System.currentTimeMillis() - this.mils;
		if (diff >= tickSpeed || diff < 0)
		{
			this.mils = System.currentTimeMillis();
			this.runTick();
		}
	}

	public void runTick()
	{
		
	}

	public boolean isrunning()
	{
		return this.running;
	}

	public void onStart()
	{
		if (testTechs)
		{
			System.out.println("--Tech Load Times--\n");

			this.doLoad();

			System.out.println("\n--Random Tech Tree Example--\n");

			this.techtests.runMulti(50, true);

			Iterator<Integer> it = this.techtests.histo.keySet().iterator();

			while (it.hasNext())
			{
				System.out.println("");

				TechItem t = TechList.get(it.next());
				System.out.println("--" + "\"" + t.getName() + "\"" + " Histogram--");
				System.out.println("");

				String[] out = this.techtests.getHist(t.getId()).getGraphicOutput(8);

				for (int i = 0; i < out.length; i++)
				{
					System.out.println(out[i]);
				}

				System.out.println("");

				System.out.printf("Average: %.1f%n", this.techtests.getHist(t.getId()).getAverage());
			}
		}
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
						return;
					}

					continue;
				}
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			return;
		}

		this.running = true;
	}
}
