package net.turrem.mod;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public abstract class ElementRegistry
{
	public final Class<? extends Annotation> annotationClass;
	
	public ElementRegistry(Class<? extends Annotation> annotationClass)
	{
		if (!annotationClass.isAnnotationPresent(Registrable.class))
		{
			throw new IllegalArgumentException("Annotation is not Registrable: " + annotationClass.getName());
		}
		this.annotationClass = annotationClass;
		ElementRegistryManager.addRegistry(this);
	}

	public abstract void registerElement(Annotation annotation, AnnotatedElement element);
}
