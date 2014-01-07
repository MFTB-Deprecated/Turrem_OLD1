package tech.turrem.math;

import branch.turrem.BranchAvailable;

public class Zero extends MathmaticalTech
{
	@Override
	public void loadBranches(int pass)
	{
		(new BranchAvailable(this, pass)).addRequired(Numerals.class, 0).push();
	}

	@Override
	public String getName(int pass)
	{
		return "Zero";
	}
	

	@Override
	public boolean isEntryLevel(int pass)
	{
		return false;
	}

	@Override
	public int getPassCount()
	{
		return 1;
	}
}
