package tech.turrem.math;

import branch.turrem.BranchAvailable;

public class Arithmetic extends MathmaticalTech
{
	@Override
	public void loadBranches(int pass)
	{
		if (pass == 0)
		{
			(new BranchAvailable(this, pass)).addRequired(Numerals.class, 0).push();
		}
		if (pass == 1)
		{
			(new BranchAvailable(this, pass)).addRequired(Arithmetic.class, 0).addRequired(Zero.class, 0).push();
		}
		if (pass == 2)
		{
			(new BranchAvailable(this, pass)).addRequired(Arithmetic.class, 0).addRequired(Ratio.class, 1).push();
		}
	}

	@Override
	public String getName(int pass)
	{
		switch (pass)
		{
			case 0:
				return "Whole-Number Arithmetic";
			case 1:
				return "Integer Arithmetic";
			case 2:
				return "Arithmetic";
			default:
				return "Arithmetic";
		}
	}

	@Override
	public boolean isEntryLevel(int pass)
	{
		return false;
	}

	@Override
	public int getPassCount()
	{
		return 3;
	}
}
