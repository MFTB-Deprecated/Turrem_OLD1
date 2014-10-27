package net.turrem.app.utils;

import java.util.ArrayList;

import java.io.File;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import net.turrem.blueutils.JarExplore;

public class JarLoader
{
	protected File theFile;
	protected JarExplore explore;
	protected URLClassLoader theLoader = null;
	protected ClassLoader theSuperLoader;
	
	private JarLoader(File jarFile, ClassLoader loader) throws IOException
	{
		this.theFile = jarFile;
		this.theSuperLoader = loader;
		this.explore = JarExplore.newInstance(jarFile);
	}
	
	public ArrayList<Class<?>> loadJarClassFiles()
	{
		try
		{
			this.theLoader = new URLClassLoader(new URL[] { this.theFile.toURI().toURL() }, this.theSuperLoader);
		}
		catch (MalformedURLException e)
		{
			return null;
		}
		
		if (this.theLoader != null && this.explore != null)
		{
			ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
			ArrayList<String> classNames = this.explore.getClassNames();
			if (classNames == null)
			{
				return classes;
			}
			for (String className : classNames)
			{
				Class<?> clss = null;
				try
				{
					clss = this.theLoader.loadClass(className);
				}
				catch (ClassNotFoundException e)
				{
					System.err.println(className + ".class could not be loaded from \"" + this.theFile.getName() + "\"");
					e.printStackTrace();
					clss = null;
				}
				if (clss != null)
				{
					classes.add(clss);
				}
			}
			return classes;
		}
		return null;
	}
	
	public String getClassName(String name)
	{
		String className = name.substring(0, name.lastIndexOf('.'));
		className = className.replace('/', '.');
		return className;
	}
	
	public static JarLoader newInstance(File jarFile, ClassLoader loader)
	{
		if (!jarFile.exists())
		{
			return null;
		}
		try
		{
			return new JarLoader(jarFile, loader);
		}
		catch (IOException e)
		{
			return null;
		}
	}
}
