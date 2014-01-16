package zap.turrem.core.tech.list;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import zap.turrem.core.tech.Tech;
import zap.turrem.core.tech.item.TechItem;

public class TechList
{
	private static List<TechItem> techList = new ArrayList<TechItem>();
	private static HashMap<String, Integer> techIds = new HashMap<String, Integer>();

	/**
	 * Gets the number of techs in the registry list
	 * @return The number of techs int this list
	 */
	public static int getSize()
	{
		return techList.size();
	}

	/**
	 * Adds a tech to this list
	 * @param tech The TechItem to add
	 * @return The tech's new index
	 */
	public static int addTech(TechItem tech)
	{
		int id = techList.size();
		techList.add(tech);
		techIds.put(tech.getIdentifier(), id);
		return id;
	}

	public static TechItem get(int index)
	{
		return techList.get(index);
	}

	public static int getIndex(String idetifier)
	{
		Integer i = techIds.get(idetifier);
		if (i != null)
		{
			return i;
		}
		return -1;
	}

	public static int getIndex(TechItem tech)
	{
		return getIndex(tech.getIdentifier());
	}

	public static int getIndex(Class<? extends Tech> techclass, int pass)
	{
		return getIndex(Tech.getClassIdentifier(techclass, pass));
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
		return getTech(Tech.getClassIdentifier(techclass, pass));
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

	/**
	 * Loads ab individual tech class
	 * @param theClass The tech class
	 * @return Any errors that occur
	 */
	public static LoadErrors loadTechClass(Class<?> theClass)
	{
		if (!Tech.class.isAssignableFrom(theClass))
		{
			return LoadErrors.NotTech;
		}
		if (Modifier.isAbstract(theClass.getModifiers()))
		{
			return LoadErrors.Abstract;
		}
		Object obj;
		try
		{
			obj = theClass.newInstance();
		}
		catch (Exception e)
		{
			return LoadErrors.CouldNotConstruct;
		}
		if (obj != null)
		{
			if (obj instanceof Tech)
			{
				Tech tech = (Tech) obj;
				return loadTech(tech);
			}
		}
		return LoadErrors.Other;
	}

	public static enum LoadErrors
	{
		NotTech,
		Abstract,
		CouldNotConstruct,
		TechNull,
		Other,
		Success;
	}

	/**
	 * Loads a tech from the 'static' tech object
	 * @param tech The tech object
	 * @return
	 */
	public static LoadErrors loadTech(Tech tech)
	{
		if (tech != null)
		{
			int n = tech.getPassCount();
			for (int i = 0; i < n; i++)
			{
				TechItem item = tech.buildItem(i);
				item.push();
			}
			return LoadErrors.Success;
		}
		return LoadErrors.TechNull;
	}

	/**
	 * Iterates through all registered techs and calls TechItem.loadBranches() on each one
	 */
	public static void loadBranches()
	{
		for (int i = 0; i < techList.size(); i++)
		{
			TechItem item = techList.get(i);
			item.loadBranches();
		}
	}
}
