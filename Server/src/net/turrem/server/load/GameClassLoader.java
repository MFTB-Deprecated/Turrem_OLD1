package net.turrem.server.load;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

public class GameClassLoader extends URLClassLoader
{
	public GameClassLoader(URL[] arg0, ClassLoader arg1, URLStreamHandlerFactory arg2)
	{
		super(arg0, arg1, arg2);
	}

	public GameClassLoader(URL[] arg0, ClassLoader arg1)
	{
		super(arg0, arg1);
	}

	public GameClassLoader(URL[] arg0)
	{
		super(arg0);
	}
}