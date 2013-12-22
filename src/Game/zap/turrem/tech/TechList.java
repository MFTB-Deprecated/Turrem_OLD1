package zap.turrem.tech;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TechList
{
	protected static List<TechBase> techlist = new ArrayList<TechBase>();
	protected static Map<String, Integer> identifierMap = new HashMap<String, Integer>();

	public static int addTech(TechBase tech, String name)
	{
		int id = techlist.size();
		techlist.add(tech);
		identifierMap.put(name, id);
		return id;
	}
	
	public static Tech getTech(Class<? extends Tech> tech, int pass)
	{
		String name = getTechIdentifier(tech, pass);
		return getTech(name);
	}
	
	public static String getTechIdentifier(Class<? extends Tech> tech, int pass)
	{
		String name = Tech.getTechIdentifier(tech, pass);
		return name;
	}
	
	public static String getTechIdentifier(Class<? extends Tech> tech)
	{
		return getTechIdentifier(tech, 0);
	}
	
	public static Tech getTech(Class<? extends Tech> tech)
	{
		return getTech(tech, 0);
	}
	
	public static int getIndex(Class<? extends Tech> tech, int pass)
	{
		return getIndex(getTechIdentifier(tech, pass));
	}
	
	public static int getIndex(Class<? extends Tech> tech)
	{
		return getIndex(getTechIdentifier(tech));
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
	
	public static Tech getTech(String tech)
	{
		return techlist.get(getIndex(tech));
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
