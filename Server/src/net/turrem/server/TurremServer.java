package net.turrem.server;

import java.io.File;

import net.turrem.EnumSide;
import net.turrem.entity.EntityArticleRegistry;
import net.turrem.entity.RegisterEntityArticle;
import net.turrem.mod.ModLoader;
import net.turrem.mod.NotedElementRegistryRegistry.NotedElementRegistryRegistryWrapper;
import net.turrem.server.network.NetworkRoom;
import net.turrem.server.world.World;
import net.turrem.server.world.biome.BiomeRegistry;
import net.turrem.server.world.biome.RegisterBiome;
import net.turrem.server.world.morph.GeomorphRegistry;
import net.turrem.server.world.morph.RegisterGeomorph;
import net.turrem.server.world.morph.RegisterStartingGeomorph;
import net.turrem.server.world.morph.StartingGeomorphRegistry;
import net.turrem.server.world.settings.WorldSettings;

public class TurremServer
{
	public final File theGameDir;
	public final File theSaveDir;
	
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
	
	public NetworkRoom theNetwork;
	
	public ModLoader modLoader;
	private NotedElementRegistryRegistryWrapper elementRegistryRegistry;
	
	public EntityArticleRegistry entityArticleRegistry;
	public StartingGeomorphRegistry startingGeomorphRegistry;
	
	public TurremServer(String dir, String save)
	{
		this.theGameDir = new File(dir);
		System.out.println("Game Dir: " + this.theGameDir.getAbsolutePath());
		this.theSaveDir = new File(save);
		System.out.println("Save File: " + this.theSaveDir.getAbsolutePath());
	}
	
	protected void run()
	{
		this.elementRegistryRegistry = new NotedElementRegistryRegistryWrapper();
		this.modLoader = new ModLoader(new File(this.theGameDir, "mods"), EnumSide.SERVER);
		
		this.entityArticleRegistry = new EntityArticleRegistry(EnumSide.SERVER);
		this.startingGeomorphRegistry = new StartingGeomorphRegistry();
		GeomorphRegistry geomorphs = new GeomorphRegistry();
		BiomeRegistry biomes = new BiomeRegistry();
		
		this.modLoader.findMods();
		this.modLoader.loadModClasses(this.getClass().getClassLoader());
		
		this.elementRegistryRegistry.addRegistry(this.entityArticleRegistry, RegisterEntityArticle.class);
		this.elementRegistryRegistry.addRegistry(geomorphs, RegisterGeomorph.class);
		this.elementRegistryRegistry.addRegistry(biomes, RegisterBiome.class);
		this.elementRegistryRegistry.addRegistry(this.startingGeomorphRegistry, RegisterStartingGeomorph.class);
		
		this.modLoader.loadMods(this.elementRegistryRegistry.getRegistry());
		
		this.onRun();
		this.runloop();
	}
	
	public void onRun()
	{
		WorldSettings set = new WorldSettings(null);
		set.settings.setShort("meshLayers", (short) 5);
		this.theWorld = new World(this.theSaveDir, System.currentTimeMillis(), set, this);
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
				this.timeoff *= 0.98F;
				long time = System.currentTimeMillis();
				
				this.timeoff += (time - this.lastTime) - 100;
				if (this.timeoff < 0)
				{
					this.timeoff = 0;
				}
				if (this.timeoff > 1000)
				{
					this.timeoff = 1000;
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
