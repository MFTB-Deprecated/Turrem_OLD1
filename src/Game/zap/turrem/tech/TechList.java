package zap.turrem.tech;

import java.util.Iterator;

import zap.turrem.loadable.JarLoader;
import zap.turrem.loadable.LoadableFeatureList;
import zap.turrem.tech.branch.Branch;
import zap.turrem.tech.branch.BranchList;

public class TechList
{
	public static LoadableFeatureList<TechBase> list = new LoadableFeatureList<TechBase>();
	public static JarLoader<TechBase> loader = new JarLoader<TechBase>();

	public static void loadBranches()
	{
		Iterator<TechBase> it = list.getIterator();
		while (it.hasNext())
		{
			it.next().loadAllBranches();
		}
		System.out.println("Loaded " + BranchList.branchCount() + " branches");
		int dif = Branch.instanceCount - BranchList.branchCount();
		if (dif != 0)
		{
			System.out.println("Warning! " + dif + " branch(es) are not pushed to list");
		}
	}

	public static boolean loadTechClass(Class<?> stone)
	{
		try
		{
			Class<TechBase> tech = (Class<TechBase>) stone;
			list.loadClass(tech, loader);
			return true;
		}
		catch (Exception e)
		{

		}
		return false;
	}
}
