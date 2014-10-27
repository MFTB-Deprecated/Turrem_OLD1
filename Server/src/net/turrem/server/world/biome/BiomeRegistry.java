package net.turrem.server.world.biome;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import java.lang.annotation.Annotation;

import net.turrem.mod.ModInstance;
import net.turrem.mod.registry.ClassWithFactoryRegistry;
import net.turrem.server.world.morph.GeomorphRegistry;

public class BiomeRegistry extends ClassWithFactoryRegistry
{
	private static HashMap<String, Biome> registry = new HashMap<String, Biome>();
	
	private final static List<Class<?>[]> valadParameters = new ArrayList<Class<?>[]>();
	
	public BiomeRegistry()
	{
		super(Biome.class);
	}
	
	public static Biome getBiome(String id)
	{
		return registry.get(id);
	}
	
	public static Collection<Biome> getBiomes()
	{
		return registry.values();
	}
	
	@Override
	protected List<Class<?>[]> getPossibleFactoryParameters()
	{
		return valadParameters;
	}
	
	@Override
	protected Object[] getArgs(int argsType, Annotation annotation, ModInstance mod)
	{
		RegisterBiome reg = (RegisterBiome) annotation;
		switch (argsType)
		{
			case 0:
				return new Object[] {};
			case 1:
				return new Object[] { reg.id() };
			default:
				return new Object[] {};
		}
	}
	
	@Override
	protected void addItem(Object item, ModInstance mod)
	{
		Biome biome = (Biome) item;
		biome.mod = mod;
		addBiome(biome);
		GeomorphRegistry.addMorph(biome);
	}
	
	public static void addBiome(Biome biome)
	{
		if (registry.put(biome.getId(), biome) != null)
		{
			System.out.printf("A biome with id %s was already registered, it will be overridden.%n", biome.getId());
		}
	}
	
	static
	{
		valadParameters.add(new Class<?>[] {});
		valadParameters.add(new Class<?>[] { String.class });
	}
}
