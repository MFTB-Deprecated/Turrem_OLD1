package net.turrem.server.load;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

public class JarExplore
{
	protected File theFile;
	protected JarFile jar;
	protected GameClassLoader theLoader;
	
	private JarExplore(File jarFile, GameClassLoader loader) throws IOException
	{
		this.theFile = jarFile;
		this.theLoader = loader;
		this.jar = new JarFile(this.theFile);
	}
	
	public static JarExplore newInstance(File jarFile, GameClassLoader loader)
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
