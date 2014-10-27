package net.turrem.app.server;

import java.io.File;
import java.io.IOException;

import java.awt.image.BufferedImage;

import net.turrem.app.EnumSide;
import net.turrem.app.entity.EntityArticleRegistry;
import net.turrem.app.entity.RegisterEntityArticle;
import net.turrem.app.mod.ModLoader;
import net.turrem.app.mod.NotedElementRegistryRegistry.NotedElementRegistryRegistryWrapper;
import net.turrem.app.server.network.NetworkRoom;
import net.turrem.app.server.world.World;
import net.turrem.app.server.world.biome.BiomeRegistry;
import net.turrem.app.server.world.biome.RegisterBiome;
import net.turrem.app.server.world.morph.GeomorphRegistry;
import net.turrem.app.server.world.morph.RegisterGeomorph;
import net.turrem.app.server.world.morph.RegisterStartingGeomorph;
import net.turrem.app.server.world.morph.StartingGeomorphRegistry;
import net.turrem.app.server.world.settings.WorldSettings;

import javax.imageio.ImageIO;

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
	
	public static float[] grid;
	
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
		
		long t = System.nanoTime();
		grid = this.theWorld.getGrid(64, 64);
		t = System.nanoTime() - t;
		BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < grid.length; i++)
		{
			float f = grid[i];
			f -= 0.5;
			f /= 1.3;
			if (f < 0)
			{
				f = 0;
			}
			if (f > 1)
			{
				f = 1;
			}
			int c = (int) (f * 255);
			img.setRGB(i % 64, i / 64, c << 16 | c << 8 | c);
		}
		File outputfile = new File("gridout.png");
		try
		{
			ImageIO.write(img, "png", outputfile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.println(t / grid.length);
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
