package net.turrem.mod;

import java.util.Collection;
import java.util.HashMap;
import java.util.jar.JarFile;

import java.io.File;
import java.io.IOException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.turrem.EnumSide;
import net.turrem.utils.JarExplore;

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
						System.out.printf("Mod [%s] is already registered! This is a bug!%n", id);
						break;
					}
					System.out.printf("Found mod.info for [%s].%n", id);
					ModInstance mod = null;
					try
					{
						mod = new ModInstance(id, Files.toString(info, Charsets.UTF_8));
					}
					catch (IOException io)
					{
						System.out.printf("Failed to load mod.info for [%s]. An IOException occurred.%n", id);
					}
					catch (InvalidSyntaxException e)
					{
						System.out.printf("Failed to load mod.info for [%s]. File was not valid JSON.%n", id);
					}
					if (mod != null)
					{
						this.mods.put(id, mod);
					}
				}
			}
		}
	}

	public void loadMods(NotedElementVisitorRegistry notedElements)
	{
		for (String id : this.mods.keySet())
		{
			Collection<Class<?>> claz = null;
			try
			{
				claz = JarExplore.newInstance(this.getModJar(id)).getLoadedClasses();
			}
			catch (IOException e)
			{
				System.out.printf("Failed to get class list for [%s].%n", id);
			}
			if (claz != null)
			{
				this.preVisitLoad(notedElements, claz);
			}
		}
		for (String id : this.mods.keySet())
		{
			this.getModJar(id).entries()
		}
	}

	protected JarFile getModJar(String id) throws IOException
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

	protected void preVisitLoad(NotedElementVisitorRegistry registry, Collection<Class<?>> claz)
	{
		for (Class<?> clas : claz)
		{
			for (Method met : clas.getDeclaredMethods())
			{
				if (met.isAnnotationPresent(RegisterVisitors.class))
				{
					String name = clas.getName() + "." + met.getName() + "()";
					if (!Modifier.isStatic(met.getModifiers()))
					{
						System.out.printf("Method %s has @RegisterVisitors, but is not static.%n", name);
					}
					else if (met.getParameterTypes().length != 1)
					{
						System.out.printf("Method %s has @RegisterVisitors, but requires %d parameters. It should require a single parameter.%n", name, met.getParameterTypes().length);
					}
					else if (!met.getParameterTypes()[0].isInstance(NotedElementVisitorRegistry.class))
					{
						System.out.printf("Method %s has @RegisterVisitors, but takes a parameter that is not a NotedElementVisitorRegistry.%n", name);
					}
					else
					{
						try
						{
							met.invoke(null, registry);
						}
						catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
						{
							System.out.printf("Method %s has @RegisterVisitors and is correctly declared, but could not be invoked because an %s was thrown.%n", name, e.getClass().getSimpleName());
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			}
		}
	}

}
