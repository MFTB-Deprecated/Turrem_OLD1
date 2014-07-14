package net.turrem.server;

import java.io.File;

import net.turrem.server.load.GameLoader;
import net.turrem.server.network.NetworkRoom;
import net.turrem.server.world.World;

public class TurremServer
{
	public final String theGameDir;
	public final String theSaveDir;

	protected long lastTime;
	protected long timeoff = 0;

	private float lasttps = 10.0F;
	private long lastTickTime;
	private int secticks = 0;

	private float tpsstore = 0.0F;
	private int tpssamp = 0;

	private long lastTPSAnTime;

	private long ticks = 0;

	public World theWorld;

	public GameLoader theLoader;

	public NetworkRoom theNetwork;

	public TurremServer(String dir, String save)
	{
		this.theGameDir = dir;
		System.out.println("dir: " + dir);
		this.theSaveDir = save;
		System.out.println("save: " + save);
	}

	protected void run()
	{
		this.onRun();
		this.runloop();
	}

	public void onRun()
	{
		this.theLoader = new GameLoader(this);
		this.theLoader.loadServerJar();
		this.theWorld = new World(this.theSaveDir, System.currentTimeMillis(), this);
		File entityjar = new File(this.theGameDir + "/jars/entity.game.jar");
		this.theLoader.loadJar(entityjar);
		this.theNetwork = new NetworkRoom(this);
	}

	public synchronized boolean acceptingClients()
	{
		return true;
	}

	public boolean isDone()
	{
		return false;
	}

	public void runloop()
	{
		this.lastTime = System.currentTimeMillis();
		this.lastTPSAnTime = System.currentTimeMillis();
		while (!this.isDone())
		{
			try
			{
				long time = System.currentTimeMillis();

				this.timeoff += (time - this.lastTime) - 100;
				if (this.timeoff < 0)
				{
					this.timeoff = 0;
				}
				if (this.timeoff > 100)
				{
					this.timeoff = 100;
				}
				this.lastTime = time;

				this.tick();

				if (0 < 100 - this.timeoff)
				{
					try
					{
						Thread.sleep(100 - this.timeoff);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				this.shutdown();
				return;
			}
		}
		this.shutdown();
	}

	public void tick()
	{
		this.theWorld.tick();

		this.ticks++;

		long dif = System.currentTimeMillis() - this.lastTickTime;
		if (dif > 1000)
		{
			this.lasttps = this.secticks * (1000.0F / dif);
			this.addTPS(this.lasttps);
			this.lastTickTime += dif;
			this.secticks = 0;
		}
		this.secticks++;

		if (this.ticks % 100 == 0)
		{
			this.resetTPS();
			long time = System.currentTimeMillis();
			System.out.println("Ticks per Second - " + (100.0F / ((time - this.lastTPSAnTime) / 1000.0F)));
			this.lastTPSAnTime = time;
		}

		this.theNetwork.networkTick();
	}

	public float getLastTPS()
	{
		return this.lasttps;
	}

	public void shutdown()
	{
		System.exit(0);
	}

	public void addTPS(float tps)
	{
		this.tpsstore += tps;
		this.tpssamp++;
	}

	public float getTPS()
	{
		if (this.tpssamp == 0)
		{
			return this.getLastTPS();
		}
		return this.tpsstore / this.tpssamp;
	}

	public void resetTPS()
	{
		this.tpssamp = 0;
		this.tpsstore = 0.0F;
	}
}
