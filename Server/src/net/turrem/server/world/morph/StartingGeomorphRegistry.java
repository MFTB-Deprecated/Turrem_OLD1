package net.turrem.server.world.morph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import net.turrem.mod.ModInstance;
import net.turrem.mod.registry.StaticMethodRegistry;
import net.turrem.server.world.settings.WorldSettings;
import net.turrem.utils.CallList;

public class StartingGeomorphRegistry extends StaticMethodRegistry
{
	private CallList mets = new CallList();
	
	@Override
	protected void registerElement(Annotation annotation, Method met, ModInstance mod)
	{
		Class<?>[] take = met.getParameterTypes();
		if (take.length != 1)
		{
			System.out.printf("Method %s has @%s but requires %d parameters, it should only take 1.%n", met.getName(), annotation.getClass().getSimpleName(), take.length);
			return;
		}
		if (take[0].isAssignableFrom(WorldSettings.class))
		{
			System.out.printf("Method %s has @%s but requires an incorrect parameter.%n", met.getName(), annotation.getClass().getSimpleName());
			return;
		}
		if (!IGeomorph.class.isAssignableFrom(met.getReturnType()))
		{
			System.out.printf("Method %s has @%s but does not return an instance of IGeomorph.%n", met.getName(), annotation.getClass().getSimpleName());
			return;
		}
		this.mets.addCall(met);
	}
	
	public Set<IGeomorph> getStarting(WorldSettings settings)
	{
		HashSet<IGeomorph> out = new HashSet<IGeomorph>();
		Iterator<Method> ms = this.mets.getCalls();
		while (ms.hasNext())
		{
			Method met = ms.next();
			try
			{
				out.add((IGeomorph) met.invoke(null, settings));
			}
			catch (Exception e)
			{
				System.out.printf("Method %s threw %s when invoked.%n");
				e.printStackTrace();
			}
		}
		return out;
	}
}
