package net.turrem.client.game.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import net.turrem.client.game.world.ClientWorld;
import net.turrem.utils.nbt.NBTCompound;

public class EntityRegistry
{
	private static HashMap<String, Class<? extends ClientEntity>> registry = new HashMap<String, Class<? extends ClientEntity>>();

	public static void register(String id, Class<? extends ClientEntity> entity)
	{
		registry.put(id, entity);
	}

	public static ClientEntity newInstance(String type, ClientWorld world, long entityId, double x, double y, double z, NBTCompound data)
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
			entity = cl.getConstructor(long.class, ClientWorld.class).newInstance(entityId, world);
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			System.err.println("Could not create entity " + cl.getCanonicalName() + " (" + type + ")");
			return null;
		}
		
		entity.setPosition(x, y, z);

		entity.readNBT(data);

		return entity;
	}
}
