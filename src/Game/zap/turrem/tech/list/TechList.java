package zap.turrem.tech.list;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import zap.turrem.tech.Tech;
import zap.turrem.tech.item.JavaTechItem;
import zap.turrem.tech.item.TechItem;

public class TechList
{
	private static List<TechItem> techList = new ArrayList<TechItem>();
	private static HashMap<String, Integer> techIds = new HashMap<String, Integer>();

	public static int addTech(TechItem tech)
	{
		int id = techList.size();
		techList.add(tech);
		techIds.put(tech.getIdentifier(), id);
		return id;
	}

	public static int getIndex(String idetifier)
	{
		return techIds.get(idetifier);
	}

	public static int getIndex(TechItem tech)
	{
		return getIndex(tech.getIdentifier());
	}

	public static int getIndex(Class<? extends Tech> techclass, int pass)
	{
		Tech tech;
		try
		{
			tech = techclass.newInstance();
		}
		catch (Exception e)
		{
			return -1;
		}
		return getIndex(tech, pass);
	}

	public static int getIndex(Tech tech, int pass)
	{
		if (tech == null)
		{
			return -1;
		}
		return getIndex(tech.getIdentifier(pass));
	}

	public static TechItem getTech(Class<? extends Tech> techclass, int pass)
	{
		Tech tech;
		try
		{
			tech = techclass.newInstance();
		}
		catch (Exception e)
		{
			return null;
		}
		return getTech(tech, pass);
	}

	public static TechItem getTech(Tech tech, int pass)
	{
		if (tech == null)
		{
			return null;
		}
		return getTech(tech.getIdentifier(pass));
	}

	public static TechItem getTech(String id)
	{
		int num = getIndex(id);
		if (num >= 0 && num < techList.size())
		{
			return techList.get(num);
		}
		return null;
	}

	public static boolean loadTechClass(Class<?> theClass)
	{
		if (!Tech.class.isAssignableFrom(theClass))
		{
			return false;
		}
		if (Modifier.isAbstract(theClass.getModifiers()))
		{
			return false;
		}
		Object obj;
		try
		{
			obj = theClass.newInstance();
		}
		catch (Exception e)
		{
			return false;
		}
		if (obj != null)
		{
			if (obj instanceof Tech)
			{
				Tech tech = (Tech) obj;
				return loadTech(tech);
			}
		}
		return false;
	}
	
	public static boolean loadTech(Tech tech)
	{
		if (tech != null)
		{
			int n = tech.getPassCount();
			for (int i = 0; i < n; i++)
			{
				TechItem item = new JavaTechItem(tech, i);
				item.push();
			}
			return true;
		}
		return false;
	}
}
