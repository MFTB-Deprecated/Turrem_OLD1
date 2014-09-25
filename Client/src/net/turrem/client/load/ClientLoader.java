package net.turrem.client.load;

import java.io.File;
import java.util.ArrayList;

import net.turrem.client.Turrem;
import net.turrem.client.load.control.GameEntity;
import net.turrem.utils.JarExplore;
import net.turrem.utils.JarLoader;

public class ClientLoader implements IClientLoad
{
	protected Turrem theGame;

	public ClassLoader theClassLoader;

	public ClientLoader(Turrem game)
	{
		this.theGame = game;
		this.theClassLoader = this.theGame.getClass().getClassLoader();
	}

	@Override
	public void processClass(Class<?> clss)
	{
		if (clss.isAnnotationPresent(GameEntity.class))
		{
			/**
			if (ClientEntity.class.isAssignableFrom(clss))
			{
				GameEntity ann = clss.getAnnotation(GameEntity.class);
				EntityRegistry.register(ann.id(), clss.asSubclass(ClientEntity.class));
			}
			**/
		}
	}

	public void loadJar(File jar)
	{
		JarLoader explore = JarLoader.newInstance(jar, this.theClassLoader);
		ArrayList<Class<?>> classList = explore.loadJarClassFiles();
		this.processClassList(classList);
	}

	public void loadClientJar()
	{
		File jar = new File(this.theGame.theGameDir + "/jars/client.jar");
		JarExplore explore = JarExplore.newInstance(jar);
		ArrayList<Class<?>> classList = explore.getLoadedClasses();
		for (Class<?> clss : classList)
		{
			this.processTurremClass(clss);
		}
	}

	private void processTurremClass(Class<?> clss)
	{
		this.processClass(clss);
	}

	private void processClassList(ArrayList<Class<?>> classList)
	{
		for (Class<?> clss : classList)
		{
			this.processClass(clss);
		}
	}
}
