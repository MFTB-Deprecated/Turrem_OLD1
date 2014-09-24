package net.turrem.mod;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Set;

import com.google.common.collect.HashMultimap;

public class NotedElementVisitorRegistry
{
	private HashMultimap<Class<? extends Annotation>, INotedElementVisitor> visitors = HashMultimap.create();
	
	protected void addVisitor(INotedElementVisitor visitor, Class<? extends Annotation> annotation)
	{
		if (annotation == null || visitor == null)
		{
			throw new IllegalArgumentException();
		}
		if (!annotation.isAnnotationPresent(Visitable.class))
		{
			throw new IllegalArgumentException("Annotation is not Visitable: " + annotation.getName());
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
