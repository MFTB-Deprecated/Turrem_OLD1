package net.turrem.mod;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultimap;

public class NotedElementVisitorRegistry
{
	public static class NotedElementVisitorRegistryWrapper
	{
		private NotedElementVisitorRegistry registry;
		
		public NotedElementVisitorRegistryWrapper()
		{
			registry = new NotedElementVisitorRegistry();
		}
		
		public NotedElementVisitorRegistry getRegistry()
		{
			return this.registry;
		}
		
		public void addVisitor(INotedElementVisitor visitor, Class<? extends Annotation> annotation)
		{
			this.registry.addVisitor(visitor, annotation);
		}
	}
	
	private HashMultimap<Class<? extends Annotation>, INotedElementVisitor> visitors = HashMultimap.create();

	void addVisitor(INotedElementVisitor visitor, Class<? extends Annotation> annotation)
	{
		if (annotation == null || visitor == null)
		{
			throw new IllegalArgumentException();
		}
		if (!annotation.isAnnotationPresent(Visitable.class))
		{
			throw new IllegalArgumentException(String.format("Annotation %s does not have @Visitable.", annotation.getName()));
		}
		Target target = (Target) annotation.getAnnotation(Target.class);
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

	private void visitElement(AnnotatedElement element)
	{
		for (Annotation ann : element.getDeclaredAnnotations())
		{
			Set<INotedElementVisitor> viss = this.visitors.get(ann.annotationType());
			if (viss != null)
			{
				for (INotedElementVisitor vis : viss)
				{
					vis.visitElement(ann, element);
				}
			}
		}
	}

	protected void visitClass(Class<?> clas)
	{
		this.visitElement(clas);
		for (AnnotatedElement element : clas.getDeclaredConstructors())
		{
			this.visitElement(element);
		}
		for (AnnotatedElement element : clas.getDeclaredFields())
		{
			this.visitElement(element);
		}
		for (AnnotatedElement element : clas.getDeclaredMethods())
		{
			this.visitElement(element);
		}
	}
}
