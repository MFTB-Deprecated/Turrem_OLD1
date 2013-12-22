package zap.turrem.tech;

import java.util.HashMap;
import java.util.Map;

public class TechList
{
	protected static Map<String, TechBase> techlist = new HashMap<String, TechBase>();

	public static void addTech(TechBase tech, String name)
	{
		techlist.put(name, tech);
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
						String name = t.getFinalName(i);
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
