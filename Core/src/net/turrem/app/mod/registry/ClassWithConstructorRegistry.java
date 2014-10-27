package net.turrem.app.mod.registry;

import java.util.List;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.turrem.app.mod.ModInstance;

public abstract class ClassWithConstructorRegistry extends ClassRegistry
{
	private List<Class<?>[]> possible;
	
	public ClassWithConstructorRegistry(Class<?> tagetClass)
	{
		super(tagetClass);
		this.possible = this.getPossibleConstructorParameters();
	}
	
	protected abstract List<Class<?>[]> getPossibleConstructorParameters();
	
	@Override
	protected void registerElement(Annotation annotation, Class<?> clas, ModInstance mod)
	{
		Constructor<?> con = null;
		int i;
		for (i = 0; i < this.possible.size(); i++)
		{
			Class<?>[] pars = this.possible.get(i);
			try
			{
				con = clas.getConstructor(pars);
				break;
			}
			catch (NoSuchMethodException e)
			{
				
			}
		}
		if (con == null)
		{
			System.out.printf("Class %s does not have the necessary constructor(s).%n", clas.getName());
		}
		try
		{
			this.addItem(con.newInstance(this.getArgs(i, annotation, mod)), mod);
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			System.out.printf("Class %s has the necessary constructor(s), but threw %s when invoked.%n", clas.getName(), e.getClass().getSimpleName());
		}
	}
	
	protected abstract Object[] getArgs(int argsType, Annotation annotation, ModInstance mod);
	
	protected abstract void addItem(Object item, ModInstance mod);
}
