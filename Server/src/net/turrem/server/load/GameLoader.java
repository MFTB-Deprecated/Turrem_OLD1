package net.turrem.server.load;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import net.turrem.server.TurremServer;
import net.turrem.server.load.control.GameComponent;
import net.turrem.server.load.control.GameEntity;
import net.turrem.server.load.control.GameTurremEntity;
import net.turrem.server.load.control.SubscribeLoad;
import net.turrem.server.load.control.SubscribePacket;
import net.turrem.server.load.control.SubscribePacketByClass;
import net.turrem.server.network.client.ClientPacket;
import net.turrem.server.network.client.ClientPacketManager;
import net.turrem.utils.JarExplore;

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

	public void loadServerJar()
	{
		File jar = new File(this.theServer.theGameDir + "/jars/server.jar");
		JarExplore explore = JarExplore.newInstance(jar);
		ArrayList<Class<?>> classList = explore.getClassFiles();
		for (Class<?> clss : classList)
		{
			this.processTurremClass(clss);
		}
	}

	public void loadJar(File jar)
	{
		JarLoader explore = JarLoader.newInstance(jar, this.theClassLoader);
		ArrayList<Class<?>> classList = explore.loadJarClassFiles();
		this.processClassList(classList);
	}

	private void processTurremClass(Class<?> clss)
	{
		for (Method mtd : clss.getMethods())
		{
			this.processMethod(mtd, clss);
		}
		if (clss.isAnnotationPresent(GameEntity.class) || clss.isAnnotationPresent(GameTurremEntity.class))
		{
			this.loaderEntity.processClass(clss);
		}
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
		if (Modifier.isStatic(mtd.getModifiers()))
		{
			if (mtd.isAnnotationPresent(SubscribePacket.class))
			{
				SubscribePacket sub = mtd.getAnnotation(SubscribePacket.class);
				byte type = sub.type();
				if (sub.review())
				{
					ClientPacketManager.addReviewCall(mtd, type);
				}
				else
				{
					ClientPacketManager.addProcessCall(mtd, type);
				}
			}
			if (mtd.isAnnotationPresent(SubscribePacketByClass.class))
			{
				SubscribePacketByClass sub = mtd.getAnnotation(SubscribePacketByClass.class);
				Class<? extends ClientPacket> type = sub.typeClass();
				if (sub.review())
				{
					ClientPacketManager.addReviewCall(mtd, type);
				}
				else
				{
					ClientPacketManager.addProcessCall(mtd, type);
				}
			}
		}
	}
}
