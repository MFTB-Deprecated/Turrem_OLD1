package net.turrem.mod.registry;

import java.util.Collection;
import java.util.List;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.turrem.mod.ModInstance;

public abstract class NotedElementWithFactoryRegistry implements INotedElementRegister
{
	public final Class<?> tagetClass;
	
	private List<Class<?>[]> possible;
	
	public NotedElementWithFactoryRegistry(Class<?> tagetClass)
	{
		this.tagetClass = tagetClass;
		this.possible = this.getPossibleFactoryParameters();
	}
	
	protected abstract List<Class<?>[]> getPossibleFactoryParameters();
	
	@Override
	public void visitElement(Annotation annotation, AnnotatedElement element, ModInstance mod)
	{
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
	
	public void registerElement(Annotation annotation, Class<?> clas, ModInstance mod)
	{
		boolean flag = false;
		for (Method met : clas.getMethods())
		{
			if (met.isAnnotationPresent(ElementToRegisterFactory.class))
			{
				try
				{
					this.callFactory(annotation, met, mod);
					flag = true;
				}
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
				{
					System.out.printf("Method %s has @%s, but threw %s when invoked.%n", met.getName(), ElementToRegisterFactory.class.getSimpleName(), e.getClass().getSimpleName());
				}
			}
		}
		if (!flag)
		{
			System.out.printf("Class %s extends %s and is registered, but does not contain a method with @%s.%n", clas.getName(), this.tagetClass.getName(), ElementToRegisterFactory.class.getSimpleName());
		}
	}
	
	private void callFactory(Annotation ann, Method met, ModInstance mod) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		if (!Modifier.isStatic(met.getModifiers()))
		{
			System.out.printf("Method %s has @%s, but is not static.%n", met.getName(), ElementToRegisterFactory.class.getSimpleName());
			return;
		}
		Class<?> ret = met.getReturnType();
		if (!(Collection.class.isAssignableFrom(ret) || this.tagetClass.isAssignableFrom(ret)))
		{
			System.out.printf("Method %s has @%s, but does not return the correct type.%n", met.getName(), ElementToRegisterFactory.class.getSimpleName());
			return;
		}
		for (int i = 0; i < this.possible.size(); i++)
		{
			Class<?>[] pars = this.possible.get(i);
			if (this.areParametersValid(met.getParameterTypes(), pars))
			{
				Object result = met.invoke(null, this.getArgs(i, ann, mod));
				try
				{
					if (this.tagetClass.isAssignableFrom(result.getClass()))
					{
						this.addItem(result, mod);
					}
					else
					{
						for (Object out : (Collection<?>) result)
						{
							this.addItem(out, mod);
						}
					}
				}
				catch (ClassCastException e)
				{
					System.out.printf("Method %s has @%s, but does not return the correct type.%n", met.getName(), ElementToRegisterFactory.class.getSimpleName());
					return;
				}
				return;
			}
		}
		System.out.printf("Method %s has @%s, but requires invalid parameters.%n", met.getName(), ElementToRegisterFactory.class.getSimpleName());
	}
	
	protected abstract Object[] getArgs(int argsType, Annotation annotation, ModInstance mod);
	
	protected abstract void addItem(Object item, ModInstance mod);
	
	public boolean areParametersValid(Class<?>[] take, Class<?>... pass)
	{
		if (take.length != pass.length)
		{
			return false;
		}
		for (int i = 0; i < take.length; i++)
		{
			if (!take[i].isAssignableFrom(pass[i]))
			{
				return false;
			}
		}
		return true;
	}
}
