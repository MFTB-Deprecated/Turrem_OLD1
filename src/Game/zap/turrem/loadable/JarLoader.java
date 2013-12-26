package zap.turrem.loadable;

public class JarLoader<E extends Loadable>
{
	public final E newInstance(int pass, Class<? extends E> loadable)
	{
		try
		{
			return loadable.getConstructor(int.class).newInstance(pass);
		}
		catch (NoSuchMethodException e)
		{
			System.out.println("Warning! " + loadable.getName() + " is missing the proper constructor!");
			try
			{
				return loadable.getConstructor().newInstance();
			}
			catch (Exception em)
			{
				em.printStackTrace();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	protected final int getPassNum(Class<? extends E> loadable)
	{
		return getStaticPassNum(loadable);
	}

	protected final String getIdentifier(Class<? extends E> loadable, int pass)
	{
		return getStaticIdentifier(loadable, pass);
	}
	
	public static final int getStaticPassNum(Class<? extends Loadable> loadable)
	{
		try
		{
			return (int) loadable.getMethod("numPass").invoke(null);
		}
		catch (Exception e)
		{
			return 1;
		}
	}
	
	public static final String getStaticIdentifier(Class<? extends Loadable> loadable, int pass)
	{
		String name = loadable.getName();
		int numpass = getStaticPassNum(loadable);
		if (numpass > 1)
		{
			pass = pass % numpass;
			name += "#" + pass;
		}
		else
		{
			pass = 0;
		}
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

	public final Loadable[] newInstances(Class<? extends E> loadable)
	{
		int passnum = getPassNum(loadable);
		Loadable[] insts = new Loadable[passnum];
		for (int i = 0; i < passnum; i++)
		{
			insts[i] = this.newInstance(i, loadable);
		}
		return insts;
	}
}
