package net.turrem.mod;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.HashSet;

public class ElementRegistryManager
{
	private static HashMap<Class<? extends Annotation>, HashSet<ElementRegistry>> registries = new HashMap<Class<? extends Annotation>, HashSet<ElementRegistry>>();
	
	public synchronized static void addRegistry(ElementRegistry registry)
	{
		Class<? extends Annotation> annotation = registry.annotationClass;
		if (annotation == null || registry == null)
		{
			throw new IllegalArgumentException();
		}
		HashSet<ElementRegistry> regs = registries.get(annotation);
		if (regs == null)
		{
			regs = new HashSet<ElementRegistry>();
			registries.put(annotation, regs);
		}
		regs.add(registry);
	}
	
	public synchronized static void regesterElement(AnnotatedElement element)
	{
		for (Annotation ann : element.getDeclaredAnnotations())
		{
			HashSet<ElementRegistry> regs = registries.get(ann);
			if (regs != null)
			{
				for (ElementRegistry reg : regs)
				{
					reg.registerElement(ann, element);
				}
			}
		}
	}
}
