package net.turrem.mod;

import java.util.ArrayList;
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
		Collection<Class<?>> claz = new ArrayList<Class<?>>();
		for (String id : this.mods.keySet())
		{
			try
			{
				claz.addAll(JarExplore.newInstance(this.getModJar(id)).getLoadedClasses());
			}
			catch (IOException e)
			{
				System.out.printf("Failed to get class list for [%s].%n", id);
			}
		}
		this.onPreVisitLoad(notedElements, claz);
		this.onLoad(notedElements, claz);
		this.onPostLoad(claz);
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

	protected void onLoad(NotedElementVisitorRegistry registry, Collection<Class<?>> claz)
	{
		for (Class<?> clas : claz)
		{
			registry.visitClass(clas);
			for (Method met : clas.getDeclaredMethods())
			{
				if (met.isAnnotationPresent(OnLoad.class))
				{
					String name = met.getName();
					if (!Modifier.isStatic(met.getModifiers()))
					{
						System.out.printf("Method %s has @OnLoad, but is not static.%n", name);
					}
					else if (met.getParameterTypes().length != 0)
					{
						System.out.printf("Method %s has @OnLoad, but requires %d parameters. It should not require any parameters.%n", name, met.getParameterTypes().length);
					}
					else
					{
						try
						{
							met.invoke(null);
						}
						catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
						{
							System.out.printf("Method %s has @OnLoad and is correctly declared, but threw %s when invoked.%n", name, e.getClass().getSimpleName());
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
	
	protected void onPostLoad(Collection<Class<?>> claz)
	{
		for (Class<?> clas : claz)
		{
			for (Method met : clas.getDeclaredMethods())
			{
				if (met.isAnnotationPresent(OnPostLoad.class))
				{
					String name = met.getName();
					if (!Modifier.isStatic(met.getModifiers()))
					{
						System.out.printf("Method %s has @OnPostLoad, but is not static.%n", name);
					}
					else if (met.getParameterTypes().length != 0)
					{
						System.out.printf("Method %s has @OnPostLoad, but requires %d parameters. It should not require any parameters.%n", name, met.getParameterTypes().length);
					}
					else
					{
						try
						{
							met.invoke(null);
						}
						catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
						{
							System.out.printf("Method %s has @OnPostLoad and is correctly declared, but threw %s when invoked.%n", name, e.getClass().getSimpleName());
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
	
	protected void onPreVisitLoad(NotedElementVisitorRegistry registry, Collection<Class<?>> claz)
	{
		for (Class<?> clas : claz)
		{
			for (Method met : clas.getDeclaredMethods())
			{
				String name = met.getName();
				if (met.isAnnotationPresent(PreRegister.class))
				{
					if (!Modifier.isStatic(met.getModifiers()))
					{
						System.out.printf("Method %s has @RegisterVisitors, but is not static.%n", name);
					}
					else if (met.getParameterTypes().length != 1)
					{
						System.out.printf("Method %s has @PreRegister, but requires %d parameters. It should require a single parameter.%n", name, met.getParameterTypes().length);
					}
					else if (!met.getParameterTypes()[0].isAssignableFrom(NotedElementVisitorRegistry.class))
					{
						System.out.printf("Method %s has @PreRegister, but takes a parameter that is not assignable from NotedElementVisitorRegistry.%n", name);
					}
					else
					{
						try
						{
							met.invoke(null, registry);
						}
						catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
						{
							System.out.printf("Method %s has @RegisterVisitors and is correctly declared, but threw %s when invoked.%n", name, e.getClass().getSimpleName());
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
				if (met.isAnnotationPresent(OnPreLoad.class))
				{
					if (!Modifier.isStatic(met.getModifiers()))
					{
						System.out.printf("Method %s has @OnPreLoad, but is not static.%n", name);
					}
					else if (met.getParameterTypes().length != 0)
					{
						System.out.printf("Method %s has @OnPreLoad, but requires %d parameters. It should not require any parameters.%n", name, met.getParameterTypes().length);
					}
					else
					{
						try
						{
							met.invoke(null);
						}
						catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
						{
							System.out.printf("Method %s has @OnPreLoad and is correctly declared, but but threw %s when invoked.%n", name, e.getClass().getSimpleName());
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
