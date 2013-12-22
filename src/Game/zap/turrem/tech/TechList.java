package zap.turrem.tech;

public class TechList
{
	public static int addTech(Tech tech, String name)
	{
		return -1;
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
					String name = techs[i].getFinalName(i);
					addTech(techs[i], name);
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
