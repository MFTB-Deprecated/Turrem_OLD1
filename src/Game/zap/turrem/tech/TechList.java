package zap.turrem.tech;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TechList
{
	protected static List<TechBase> techlist = new ArrayList<TechBase>();
	protected static Map<String, Integer> identifierMap = new HashMap<String, Integer>();

	public static void loadBranches()
	{
		Iterator<TechBase> it = techlist.iterator();
		while (it.hasNext())
		{
			it.next().loadBranches();
		}
	}
	
	public static int techCount()
	{
		return techlist.size();
	}
	
	@Deprecated
	public static void callMethod(String methoud, Object... pars)
	{
		Iterator<TechBase> it = techlist.iterator();
		Class<?>[] parclass = new Class[pars.length];
		for (int i = 0; i < pars.length; i++)
		{
			parclass[i] = pars[i].getClass();
		}
		while (it.hasNext())
		{
			try
			{
				TechBase.class.getMethod(methoud, parclass).invoke(it.next(), pars);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static int addTech(TechBase tech, String name)
	{
		int id = techlist.size();
		techlist.add(tech);
		identifierMap.put(name, id);
		return id;
	}
	
	public static TechBase getTech(Class<? extends Tech> tech, int pass)
	{
		String name = getIdentifier(tech, pass);
		return getTech(name);
	}
	
	public static String getIdentifier(Class<? extends Tech> tech, int pass)
	{
		String name = Tech.getTechIdentifier(tech, pass);
		return name;
	}
	
	public static TechBase getTech(int index)
	{
		return techlist.get(index);
	}
	
	public static String getIdentifier(int index)
	{
		TechBase t = getTech(index);
		return t.getIdentifier();
	}
	
	public static String getIdentifier(Class<? extends Tech> tech)
	{
		return getIdentifier(tech, 0);
	}
	
	public static TechBase getTech(Class<? extends Tech> tech)
	{
		return getTech(tech, 0);
	}
	
	public static int getIndex(Class<? extends Tech> tech, int pass)
	{
		return getIndex(getIdentifier(tech, pass));
	}
	
	public static int getIndex(Class<? extends Tech> tech)
	{
		return getIndex(getIdentifier(tech));
	}
	
	public static int getIndex(TechBase tech)
	{
		return getIndex(tech.getIdentifier());
	}
	
	public static int getIndex(Tech tech, int pass)
	{
		return getIndex(tech.getIdentifier(pass));
	}
	
	public static int getIndex(String tech)
	{
		return identifierMap.get(tech);
	}
	
	public static TechBase getTech(String tech)
	{
		return getTech(getIndex(tech));
	}

	public static boolean loadTechClass(Class<?> tech)
	{
		try
		{
			Object ret = tech.getMethod("newInstances", Class.class).invoke(null, tech);
			if (ret != null && ret instanceof Tech[])
			{
				Tech[] techs = (Tech[]) ret;
				for (int i = 0; i < techs.length; i++)
				{
					Tech t = techs[i];
					if (t instanceof TechBase)
					{
						String name = t.getIdentifier(i);
						addTech((TechBase) t, name);
					}
				}
			}
			else
			{
				return false;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
