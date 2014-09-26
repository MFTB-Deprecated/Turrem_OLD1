package net.turrem.mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarFile;

import java.io.File;
import java.io.IOException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import net.turrem.EnumSide;
import net.turrem.utils.JarExplore;

import com.google.common.base.Charsets;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.io.Files;

import argo.saj.InvalidSyntaxException;

public class ModLoader
{
	private HashMap<String, ModInstance> mods = new HashMap<String, ModInstance>();
	private final EnumSide side;
	private final File modDirectory;
	public URLClassLoader modClassLoader;

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
	
	public void loadModClasses(ClassLoader parent)
	{
		ArrayList<URL> jarlist = new ArrayList<URL>();
		for (ModInstance mod : this.mods.values())
		{
			File jar = this.getModJarFile(mod);
			if (jar.exists())
			{
				try
				{
					jarlist.add(jar.toURI().toURL());
				}
				catch (MalformedURLException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				System.out.printf("The %s jar for mod %s does not exist.", this.side.name().toLowerCase(), mod.identifier);
			}
		}
		URL[] jars = new URL[jarlist.size()];
		jars = jarlist.toArray(jars);
		this.modClassLoader = URLClassLoader.newInstance(jars, parent);
	}

	public void loadMods(NotedElementVisitorRegistry notedElements)
	{
		ArrayListMultimap<ModInstance, Class<?>> claz = ArrayListMultimap.create();
		for (ModInstance mod : this.mods.values())
		{
			try
			{
				claz.putAll(mod, JarExplore.newInstance(this.getModJar(mod)).getLoadedClasses(this.modClassLoader));
			}
			catch (IOException e)
			{
				System.out.printf("Failed to get class list for [%s].%n", mod.identifier);
			}
		}
		this.onPreVisitLoad(notedElements, claz);
		this.onLoad(notedElements, claz);
		this.onPostLoad(claz);
	}

	protected JarFile getModJar(String id) throws IOException
	{
		return new JarFile(this.getModJarFile(id));
	}
	
	protected File getModJarFile(String id)
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
		return new File(this.modDirectory, id + jar);
	}

	protected JarFile getModJar(ModInstance mod) throws IOException
	{
		return this.getModJar(mod.identifier);
	}
	
	protected File getModJarFile(ModInstance mod)
	{
		return this.getModJarFile(mod.identifier);
	}

	protected void onLoad(NotedElementVisitorRegistry registry, ArrayListMultimap<ModInstance, Class<?>> map)
	{
		for (ModInstance mod : map.keySet())
		{
			List<Class<?>> claz = map.get(mod);
			for (Class<?> clas : claz)
			{
				registry.visitClass(clas, mod);
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
	}

	protected void onPostLoad(ArrayListMultimap<ModInstance, Class<?>> map)
	{
		for (ModInstance mod : map.keySet())
		{
			List<Class<?>> claz = map.get(mod);
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
	}

	protected void onPreVisitLoad(NotedElementVisitorRegistry registry, ArrayListMultimap<ModInstance, Class<?>> map)
	{
		for (ModInstance mod : map.keySet())
		{
			List<Class<?>> claz = map.get(mod);
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

}
