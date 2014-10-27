package net.turrem.app.mod;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import net.turrem.app.mod.registry.INotedElementRegistry;
import net.turrem.app.mod.registry.Registrable;

import com.google.common.collect.HashMultimap;

public class NotedElementRegistryRegistry
{
	public static class NotedElementRegistryRegistryWrapper
	{
		private NotedElementRegistryRegistry registry;
		
		public NotedElementRegistryRegistryWrapper()
		{
			this.registry = new NotedElementRegistryRegistry();
		}
		
		public NotedElementRegistryRegistry getRegistry()
		{
			return this.registry;
		}
		
		public void addRegistry(INotedElementRegistry visitor, Class<? extends Annotation> annotation)
		{
			this.registry.addVisitor(visitor, annotation);
		}
	}
	
	private HashMultimap<Class<? extends Annotation>, INotedElementRegistry> visitors = HashMultimap.create();
	
	void addVisitor(INotedElementRegistry visitor, Class<? extends Annotation> annotation)
	{
		if (annotation == null || visitor == null)
		{
			throw new IllegalArgumentException();
		}
		if (!annotation.isAnnotationPresent(Registrable.class))
		{
			throw new IllegalArgumentException(String.format("Annotation %s does not have @Registrable.", annotation.getName()));
		}
		Target target = annotation.getAnnotation(Target.class);
		if (target != null)
		{
			List<ElementType> types = Arrays.asList(target.value());
			if (types.contains(ElementType.PACKAGE))
			{
				throw new IllegalArgumentException(String.format("Annotation %s can not target packages.", annotation.getName()));
			}
			if (types.contains(ElementType.LOCAL_VARIABLE))
			{
				throw new IllegalArgumentException(String.format("Annotation %s can not target local variables.", annotation.getName()));
			}
			if (types.contains(ElementType.PARAMETER))
			{
				throw new IllegalArgumentException(String.format("Annotation %s can not target local parameters.", annotation.getName()));
			}
		}
		this.visitors.put(annotation, visitor);
	}
	
	private void visitElement(AnnotatedElement element, ModInstance mod)
	{
		for (Annotation ann : element.getDeclaredAnnotations())
		{
			Set<INotedElementRegistry> viss = this.visitors.get(ann.annotationType());
			if (viss != null)
			{
				for (INotedElementRegistry vis : viss)
				{
					vis.visitElement(ann, element, mod);
				}
			}
		}
	}
	
	protected void visitClass(Class<?> clas, ModInstance mod)
	{
		this.visitElement(clas, mod);
		for (AnnotatedElement element : clas.getDeclaredConstructors())
		{
			this.visitElement(element, mod);
		}
		for (AnnotatedElement element : clas.getDeclaredFields())
		{
			this.visitElement(element, mod);
		}
		for (AnnotatedElement element : clas.getDeclaredMethods())
		{
			this.visitElement(element, mod);
		}
	}
}
