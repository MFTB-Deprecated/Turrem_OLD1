package net.turrem.server.load;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class LoadedJarExplore
{
	protected File theFile;
	protected JarFile jar = null;

	private LoadedJarExplore(File jarFile) throws IOException
	{
		this.theFile = jarFile;
		this.jar = new JarFile(this.theFile);
	}

	public ArrayList<Class<?>> loadJarClassFiles()
	{
		if (this.jar != null)
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
							theClass = Class.forName(className);
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

	public static LoadedJarExplore newInstance(File jarFile)
	{
		if (!jarFile.exists())
		{
			return null;
		}
		try
		{
			return new LoadedJarExplore(jarFile);
		}
		catch (IOException e)
		{
			return null;
		}
	}
}
