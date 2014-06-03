package net.turrem.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarExplore
{
	protected File theFile;
	protected JarFile jar = null;

	private JarExplore(File jarFile) throws IOException
	{
		this.theFile = jarFile;
		this.jar = new JarFile(this.theFile);
	}

	public ArrayList<String> getClassNames()
	{
		if (this.jar != null)
		{
			ArrayList<String> found = new ArrayList<String>();

			for (JarEntry je : Collections.list(this.jar.entries()))
			{
				if (!je.isDirectory())
				{
					String className = this.getClassName(je.getName());
					if (!className.startsWith("META-INF"))
					{
						found.add(className);
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
	
	public ArrayList<Class<?>> getClassFiles()
	{
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		ArrayList<String> classNames = this.getClassNames();
		if (classNames == null)
		{
			return classes;
		}
		for (String clsn : classNames)
		{
			Class<?> clss = null;
			try
			{
				clss = Class.forName(clsn);
			}
			catch (ClassNotFoundException e)
			{
				
			}
			if (clss != null)
			{
				classes.add(clss);
			}
		}
		return classes;
	}

	public String getClassName(String name)
	{
		String className = name.substring(0, name.lastIndexOf('.'));
		className = className.replace('/', '.');
		return className;
	}

	public static JarExplore newInstance(File jarFile)
	{
		if (!jarFile.exists())
		{
			return null;
		}
		try
		{
			return new JarExplore(jarFile);
		}
		catch (IOException e)
		{
			return null;
		}
	}
}
