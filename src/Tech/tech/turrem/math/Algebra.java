package tech.turrem.math;

import branch.turrem.BranchAvailable;

public class Algebra extends MathmaticalTech
{
	@Override
	public void loadBranches(int pass)
	{
		(new BranchAvailable(this, pass)).addRequired(Arithmetic.class, 2).addRequired(Arithmetic.class, 1).push();
	}

	@Override
	public String getName(int pass)
	{
		return "Algebra";
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
