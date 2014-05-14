package net.turrem.server.load;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarExplore
{
	protected File theFile;
	protected JarFile jar = null;
	protected GameClassLoader theLoader = null;
	protected ClassLoader theSuperLoader;

	private JarExplore(File jarFile, ClassLoader loader) throws IOException
	{
		this.theFile = jarFile;
		this.theSuperLoader = loader;
		this.jar = new JarFile(this.theFile);
	}

	public ArrayList<Class<?>> loadJarClassFiles()
	{
		try
		{
			this.theLoader = new GameClassLoader(new URL[] { this.theFile.toURI().toURL() }, this.theSuperLoader);
		}
		catch (MalformedURLException e)
		{
			return null;
		}

		if (this.theLoader != null && this.jar != null)
		{
			ArrayList<Class<?>> found = new ArrayList<Class<?>>();

			for (JarEntry je : Collections.list(this.jar.entries()))
			{
				if (!je.isDirectory())
				{
					String className = this.getClassName(je.getName());
					if (!className.startsWith("META-INF"))
					{
						Class<?> theClass;
						try
						{
							theClass = this.theLoader.loadClass(className);
						}
						catch (ClassNotFoundException e)
						{
							System.err.println(className + ".class could not be loaded from \"" + this.theFile.getName() + "\"");
							e.printStackTrace();
							theClass = null;
						}
						if (theClass != null)
						{
							found.add(theClass);
						}
					}
				}
			}

			try
			{
				this.jar.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			return found;
		}
		return null;
	}

	public String getClassName(String name)
	{
		String className = name.substring(0, name.lastIndexOf('.'));
		className = className.replace('/', '.');
		return className;
	}

	public static JarExplore newInstance(File jarFile, ClassLoader loader)
	{
		if (!jarFile.exists())
		{
			return null;
		}
		try
		{
			return new JarExplore(jarFile, loader);
		}
		catch (IOException e)
		{
			return null;
		}
	}
}
