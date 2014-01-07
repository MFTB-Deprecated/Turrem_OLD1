package zap.turrem.tech.branch;

import java.util.ArrayList;
import java.util.List;

public class BranchList
{
	private static List<Branch> branchlist = new ArrayList<Branch>();

	public static int addBranch(Branch branch)
	{
		int i = branchlist.size();
		branchlist.add(branch);
		return i;
	}
	
	public static Branch get(int i)
	{
		return branchlist.get(i);
	}

	public static int branchCount()
	{
		return branchlist.size();
	}
}