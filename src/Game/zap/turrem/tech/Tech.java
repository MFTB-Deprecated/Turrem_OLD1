package zap.turrem.tech;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Do not extend this!
 * Extend TechBase instead
 * @author Sam Sartor
 */
public abstract class Tech
{
	/**
	 * Builds the final registration name, reformats the string
	 * @param pass The tech load pass number
	 * @return The name to register the tech under
	 */
	public final String getIdentifier(int pass)
	{
		return getTechIdentifier(this.getClass(), pass);
	}
	
	/**
	 * Gets the index of this tech at a certain pass
	 * @param pass Tech pass number
	 * @return Tech's index
	 */
	public final int getId(int pass)
	{
		return TechList.getIndex(this, pass);
	}

	/**
	 * 
	 * @param pass
	 * @param ownclass
	 * @return New tech instance from tech class and pass number
	 * @throws InstantiationException Bad
	 * @throws IllegalAccessException Bad
	 * @throws IllegalArgumentException Bad
	 * @throws InvocationTargetException Bad
	 * @throws NoSuchMethodException Hint, your tech's constructor should have a single int as its parameters
	 * @throws SecurityException Bad
	 */
	public static final Tech newInstance(int pass, Class<? extends Tech> ownclass) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		try
		{
			return ownclass.getConstructor(int.class).newInstance(pass);
		}
		catch (NoSuchMethodException e)
		{
			System.out.println("Warning! " + ownclass.getName() + " is missing the proper constructor!");
			return ownclass.getConstructor().newInstance();
		}
	}

	/**
	 * How many passes tech class should have
	 * @param tech Tech's class
	 * @return Number of passes
	 * @throws IllegalAccessException Bad
	 * @throws IllegalArgumentException Bad
	 * @throws InvocationTargetException Bad
	 * @throws SecurityException Bad
	 */
	protected static final int getPassNum(Class<? extends Tech> tech)
	{
		try
		{
			return (int) tech.getMethod("numPass").invoke(null);
		}
		catch (Exception e)
		{
			return 1;
		}
	}
	
	/**
	 * Static get tech name
	 * @param tech The Tesh's class
	 * @param pass The pass number
	 * @return Tech identification name
	 */
	protected static final String getTechIdentifier(Class<? extends Tech> tech, int pass)
	{
		String name = tech.getName();
		int numpass = 1;
		try
		{
			numpass = getPassNum(tech);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
	
	/**
	 * Builds all instances of tech class
	 * @param ownclass Tech's class
	 * @return Tech instances (length equal to pass number)
	 */
	public static final Tech[] newInstances(Class<? extends Tech> ownclass)
	{
		try
		{
			int passnum = getPassNum(ownclass);
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
