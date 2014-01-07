package tech.turrem.math;

import branch.turrem.BranchAvailable;

public class Geometry extends MathmaticalTech
{
	@Override
	public void loadBranches(int pass)
	{
		(new BranchAvailable(this, pass)).addRequired(Arithmetic.class, 2).push();
	}

	@Override
	public String getName(int pass)
	{
		return "Geometry";
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
