package net.turrem.mod;

import java.util.Collection;
import java.util.List;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public abstract class NotedElementWithFactoryVisitor implements INotedElementVisitor
{
	public final Class<?> tagetClass;

	private List<Class<?>[]> poss;

	public NotedElementWithFactoryVisitor(Class<?> tagetClass)
	{
		this.tagetClass = tagetClass;
		this.poss = this.getPossibleFactoryParameters();
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
		for (Method met : clas.getMethods())
		{
			if (met.isAnnotationPresent(ToRegisterFactory.class))
			{
				try
				{
					this.callFactory(annotation, met, mod);
				}
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
				{
					System.out.printf("Method %s has @%s, but threw %s when invoked.%n", met.getName(), ToRegisterFactory.class.getSimpleName(), e.getClass().getSimpleName());
				}
			}
		}
	}

	private void callFactory(Annotation ann, Method met, ModInstance mod) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		if (!Modifier.isStatic(met.getModifiers()))
		{
			System.out.printf("Method %s has @%s, but is not static.%n", met.getName(), ToRegisterFactory.class.getSimpleName());
			return;
		}
		Class<?> ret = met.getReturnType();
		if (!(Collection.class.isAssignableFrom(ret) || this.tagetClass.isAssignableFrom(ret)))
		{
			System.out.printf("Method %s has @%s, but does not return the correct type.%n", met.getName(), ToRegisterFactory.class.getSimpleName());
			return;
		}
		for (int i = 0; i < this.poss.size(); i++)
		{
			Class<?>[] pars = this.poss.get(i);
			if (this.areParametersValid(met.getParameterTypes(), pars))
			{
				Object result = met.invoke(null, this.getArgs(i, ann, mod));
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
				return;
			}
		}
		System.out.printf("Method %s has @%s, but requires invalid parameters.%n", met.getName(), ToRegisterFactory.class.getSimpleName());
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
