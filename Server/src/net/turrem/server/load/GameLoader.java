package net.turrem.server.load;

import java.io.File;
import java.util.ArrayList;

import net.turrem.server.TurremServer;

public class GameLoader
{
	protected TurremServer theServer;
	protected EntityLoader loaderEntity;
	public ClassLoader theClassLoader;
	
	public GameLoader(TurremServer server)
	{
		this.theServer = server;
		this.theClassLoader = this.theServer.getClass().getClassLoader();
		this.loaderEntity = new EntityLoader(this);
	}
	
	public void loadJar(File jar)
	{
		JarExplore explore = JarExplore.newInstance(jar, this.theClassLoader);
		ArrayList<Class<?>> classList = explore.loadJarClassFiles();
		this.processClassList(classList);
	}
	
	private void processClassList(ArrayList<Class<?>> classList)
	{
		
	}
}
