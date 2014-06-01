package net.turrem.server.load;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import net.turrem.server.TurremServer;
import net.turrem.server.load.control.GameComponent;
import net.turrem.server.load.control.GameEntity;
import net.turrem.server.load.control.SubscribeLoad;

public class GameLoader implements IGameLoad
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
		for (Class<?> clss : classList)
		{
			this.processClass(clss);
		}
	}
	
	public EntityLoader getEntityLoader()
	{
		return loaderEntity;
	}

	@Override
	public void processClass(Class<?> clss)
	{
		if (this.processClassAnno(clss))
		{
			for (Method mtd : clss.getMethods())
			{
				this.processMethod(mtd, clss);
			}
		}
		else
		{
			System.out.println("Warning: " + clss.getName() + " is not correctly marked for class loading!");
		}
	}

	private boolean processClassAnno(Class<?> clss)
	{
		if (clss.isAnnotationPresent(GameEntity.class))
		{
			GameEntity ann = clss.getAnnotation(GameEntity.class);
			if (this.isValidFrom(ann.from()))
			{
				this.loaderEntity.processClass(clss);
				return true;
			}
		}
		if (clss.isAnnotationPresent(GameComponent.class))
		{
			GameComponent gc = clss.getAnnotation(GameComponent.class);
			return this.isValidFrom(gc.from());
		}
		return false;
	}

	private boolean isValidFrom(String from)
	{
		return from != null && !from.isEmpty();
	}

	private void processMethod(Method mtd, Class<?> clss)
	{
		if (mtd.isAnnotationPresent(SubscribeLoad.class))
		{
			try
			{
				mtd.invoke(null, this.theServer);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				System.out.println("Warning: " + clss.getName() + "." + mtd.getName() + "(...) was not called correctly!");
			}
		}
	}
}
