package net.turrem.app.mod.registry;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import net.turrem.app.mod.ModInstance;

public abstract class ClassRegistry implements INotedElementRegistry
{
	public final Class<?> tagetClass;
	
	public ClassRegistry(Class<?> tagetClass)
	{
		this.tagetClass = tagetClass;
	}
	
	@Override
	public void visitElement(Annotation annotation, AnnotatedElement element, ModInstance mod)
	{
		if (!(element instanceof Class))
		{
			System.out.printf("Tried to register an element that is not a class in a class registry.%n");
			return;
		}
		Class<?> clas = (Class<?>) element;
		if (this.tagetClass.isAssignableFrom(clas))
		{
			this.registerElement(annotation, clas, mod);
		}
		else
		{
			System.out.printf("Class %s does not extend %s.%n", clas.getName(), this.tagetClass.getName());
		}
	}
	
	protected abstract void registerElement(Annotation annotation, Class<?> clas, ModInstance mod);
}
