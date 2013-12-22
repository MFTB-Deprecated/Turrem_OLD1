package zap.turrem.tech;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class Tech
{
	public Tech(int pass)
	{
	}

	protected abstract String getName();

	protected String getName(int pass)
	{
		if (numPass() == 1)
		{
			return this.getName();
		}
		else
		{
			return this.getName() + "." + pass;
		}
	}
	
	public final String getFinalName(int pass)
	{
		String name = this.getName(pass);
		String newName = "";
		boolean cap = true;
		for (int i = 0; i < name.length(); i++)
		{
			char c = name.charAt(i);
			if (cap)
			{
				c = Character.toUpperCase(c);
			}
			cap = false;
			if (Character.isSpaceChar(c))
			{
				cap = true;
			}
			else
			{
				newName += c;
			}
		}
		return newName;
	}

	public static int numPass()
	{
		return 1;
	}

	public static final Tech newInstance(int pass, Class<? extends Tech> ownclass) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException
	{
		try
		{
			return ownclass.getConstructor(int.class).newInstance(pass);
		}
		catch (NoSuchMethodException e)
		{
			return ownclass.newInstance();
		}
	}

	public static final Tech[] newInstances(Class<? extends Tech> ownclass)
	{
		try
		{
			int passnum = (int) ownclass.getMethod("numPass").invoke(null);
			Method newinst = ownclass.getMethod("newInstance", int.class, Class.class);
			Tech[] insts = new Tech[passnum];
			for (int i = 0; i < insts.length; i++)
			{
				insts[i] = (Tech) newinst.invoke(null, i, ownclass);
			}
			return insts;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
