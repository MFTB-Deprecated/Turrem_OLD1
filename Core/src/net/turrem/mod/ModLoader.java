package net.turrem.mod;

import java.util.HashMap;
import java.util.jar.JarFile;

import java.io.File;
import java.io.IOException;

import java.nio.charset.Charset;

import net.turrem.EnumSide;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import argo.saj.InvalidSyntaxException;

public class ModLoader
{
	private HashMap<String, ModInstance> mods = new HashMap<String, ModInstance>();
	private final EnumSide side;
	private final File modDirectory;
	
	public ModLoader(File modDirectory, EnumSide side)
	{
		this.modDirectory = modDirectory;
		this.side = side;
	}
	
	public ModInstance getMod(String identifier)
	{
		return this.mods.get(identifier);
	}
	
	public void findMods()
	{
		for (File dir : this.modDirectory.listFiles())
		{
			if (dir.isDirectory())
			{
				File info = new File(dir, "mod.info");
				if (info.exists())
				{
					String id = dir.getName();
					if (mods.containsKey(id))
					{
						System.out.println("Mod [" + id + "] is already registered! This is a bug!");
						break;
					}
					System.out.println("Found Mod: [" + id + "]");
					ModInstance mod = null;
					try
					{
						mod = new ModInstance(id, Files.toString(info, Charsets.UTF_8));
					}
					catch (IOException io)
					{
						io.printStackTrace();
					}
					catch (InvalidSyntaxException e)
					{
						System.out.println("Failed to load mod.info for [" + id + "]");
					}
					if (mod != null)
					{
						this.mods.put(id, mod);
					}
				}
			}
		}
	}
	
	public JarFile getModJar(String id) throws IOException
	{
		String jar = "/";
		switch (this.side)
		{
			case SERVER:
				jar += "server";
				break;
			case CLIENT:
				jar += "client";
				break;
		}
		jar += ".jar";
		return new JarFile(new File(this.modDirectory, id + jar));
	}
}
