package net.turrem.client.game.entity;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import net.turrem.client.game.world.ClientWorld;

public class EntityRegistry
{
	private static HashMap<String, Class<? extends ClientEntity>> registry = new HashMap<String, Class<? extends ClientEntity>>();
	
	public static void register(String id, Class<? extends ClientEntity> entity)
	{
		registry.put(id, entity);
	}
	 
	public static ClientEntity newInstance(String type, ClientWorld world, int id, double x, double y, double z, DataInput extra)
	{
		Class<? extends ClientEntity> cl = registry.get(type);
		if (cl == null)
		{
			System.err.println("No entity registered to \"" + type + "\". This is a Bug!");
			return null;
		}
		ClientEntity entity;
		try
		{
			entity = cl.getConstructor(int.class, ClientWorld.class).newInstance(id, world);
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			System.err.println("Could not create entity " + cl.getCanonicalName() + " (" + type + ")");
			return null;
		}
		entity.setPosition(x, y, z);
		try
		{
			entity.readExtraData(extra);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return entity;
	}
}
