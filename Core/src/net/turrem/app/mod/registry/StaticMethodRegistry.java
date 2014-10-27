package net.turrem.app.mod.registry;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.turrem.app.mod.ModInstance;

public abstract class StaticMethodRegistry implements INotedElementRegistry
{
	@Override
	public void visitElement(Annotation annotation, AnnotatedElement element, ModInstance mod)
	{
		if (!(element instanceof Method))
		{
			System.out.printf("Tried to register an element that is not a method in a method registry.%n");
			return;
		}
		Method met = (Method) element;
		if (!Modifier.isStatic(met.getModifiers()))
		{
			System.out.printf("Method %s.%s has @%s, but is not static.%n", met.getDeclaringClass().getName(), met.getName(), annotation.getClass().getSimpleName());
			return;
		}
		this.registerElement(annotation, met, mod);
	}
	
	protected abstract void registerElement(Annotation annotation, Method met, ModInstance mod);
}
