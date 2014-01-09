package tech.turrem.math;

import branch.turrem.BranchActive;

public class Ratio extends MathmaticalTech
{
	@Override
	public void loadBranches(int pass)
	{
		if (pass == 0)
		{
			(new BranchActive(this, pass)).addRequired(Numerals.class, 0).addRequired(Arithmetic.class, 0).push();
		}
		if (pass == 1)
		{
			(new BranchActive(this, pass)).addRequired(Ratio.class, 0).addRequired(Arithmetic.class, 1).push();
		}
		if (pass == 2)
		{
			(new BranchActive(this, pass)).addRequired(Ratio.class, 1).addRequired(Zero.class, 0).push();
		}
	}

	@Override
	public String getName(int pass)
	{
		switch (pass)
		{
			case 0:
				return "Ratio Mathematics";
			case 1:
				return "Fractions";
			case 2:
				return "Decimals";
			default:
				return "Fractions";
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
