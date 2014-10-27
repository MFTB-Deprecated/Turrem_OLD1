package net.turrem.app.mod.registry;

import java.util.List;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.turrem.app.mod.ModInstance;
import net.turrem.app.utils.ReflectionUtils;

public abstract class ClassWithFactoryRegistry extends ClassRegistry
{
	private List<Class<?>[]> possible;
	
	public ClassWithFactoryRegistry(Class<?> tagetClass)
	{
		super(tagetClass);
		this.possible = this.getPossibleFactoryParameters();
	}
	
	protected abstract List<Class<?>[]> getPossibleFactoryParameters();
	
	@Override
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
				catch (Exception e)
				{
					System.out.printf("Method %s.%s has @%s, but threw %s when invoked.%n", met.getDeclaringClass().getName(), met.getName(), ElementToRegisterFactory.class.getSimpleName(), e.getClass().getSimpleName());
					e.printStackTrace();
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
			System.out.printf("Method %s.%s has @%s, but is not static.%n", met.getDeclaringClass().getName(), met.getName(), ElementToRegisterFactory.class.getSimpleName());
			return;
		}
		Class<?> ret = met.getReturnType();
		if (!ReflectionUtils.isClassNumberofClass(ret, this.tagetClass, true))
		{
			System.out.printf("Method %s.%s has @%s, but does not return the correct type.%n", met.getDeclaringClass().getName(), met.getName(), ElementToRegisterFactory.class.getSimpleName());
			return;
		}
		for (int i = 0; i < this.possible.size(); i++)
		{
			Class<?>[] pars = this.possible.get(i);
			if (ReflectionUtils.areParametersValid(met.getParameterTypes(), pars))
			{
				Object result = met.invoke(null, this.getArgs(i, ann, mod));
				for (Object out : ReflectionUtils.getNumberofClass(result, this.tagetClass, true))
				{
					this.addItem(out, mod);
				}
				return;
			}
		}
		System.out.printf("Method %s.%s has @%s, but requires invalid parameters.%n", met.getDeclaringClass().getName(), met.getName(), ElementToRegisterFactory.class.getSimpleName());
	}
	
	protected abstract Object[] getArgs(int argsType, Annotation annotation, ModInstance mod);
	
	protected abstract void addItem(Object item, ModInstance mod);
}
