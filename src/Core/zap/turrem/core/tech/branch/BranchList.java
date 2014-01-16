package zap.turrem.core.tech.branch;

import java.util.ArrayList;
import java.util.List;

/**
 * Global registry of tech branches
 */
public class BranchList
{
	private static List<Branch> branchlist = new ArrayList<Branch>();

	/**
	 * Adds a branch to the registry
	 * @param branch The branch to add
	 * @return The branch's new index
	 */
	public static int addBranch(Branch branch)
	{
		int i = branchlist.size();
		branchlist.add(branch);
		return i;
	}

	/**
	 * Gets a branch from the registry using it's index
	 * @param i Branch index
	 * @return The branch with that index
	 */
	public static Branch get(int i)
	{
		return branchlist.get(i);
	}

	/**
	 * Gets the number of branches stored in the registry
	 * @return Number of branches stored in the registry
	 */
	public static int branchCount()
	{
		return branchlist.size();
	}
}