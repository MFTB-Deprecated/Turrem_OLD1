package net.turrem.server.world.morph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import java.lang.annotation.Annotation;

import net.turrem.mod.ModInstance;
import net.turrem.mod.registry.ClassWithFactoryRegistry;

public class GeomorphRegistry extends ClassWithFactoryRegistry
{
	private static HashMap<String, IGeomorph> registry = new HashMap<String, IGeomorph>();
	
	private final static List<Class<?>[]> valadParameters = new ArrayList<Class<?>[]>();
	
	public GeomorphRegistry()
	{
		super(Geomorph.class);
	}
	
	public static IGeomorph getGeomorph(String id)
	{
		return registry.get(id);
	}
	
	public static Collection<IGeomorph> getGeomorphs()
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
		RegisterGeomorph reg = (RegisterGeomorph) annotation;
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
		Geomorph morph = (Geomorph) item;
		morph.mod = mod;
		addMorph(morph);
	}
	
	public static void addMorph(IGeomorph morph)
	{
		if (registry.put(morph.getId(), morph) != null)
		{
			System.out.printf("A geomorph with id %s was already registered, it will be overridden.%n", morph.getId());
		}
	}
	
	static
	{
		valadParameters.add(new Class<?>[] {});
		valadParameters.add(new Class<?>[] { String.class });
	}
}
