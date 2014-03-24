package net.turrem.server;

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
	
	public TurremServer(String dir, String save)
	{
		this.theGameDir = dir;
		this.theSaveDir = save;
	}

	protected void run()
	{
		this.onRun();
		this.runloop();
	}
	
	public void onRun()
	{
		
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
				if (time - this.lastTime > 100 - this.timeoff)
				{
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
			System.out.println("Ticks per Second - " + (100.0F / ((time  - this.lastTPSAnTime) / 1000.0F)));
			this.lastTPSAnTime = time;
		}
	}
	
	public float getLastTPS()
	{
		return lasttps;
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
