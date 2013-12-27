package tech.turrem.math;

import branch.turrem.BranchActive;

public class Ratio extends MathmaticalTech
{
	public Ratio(int pass)
	{
		super(pass);
	}

	@Override
	public void loadBranches()
	{
		if (this.pass == 0)
		{
			(new BranchActive(this)).addRequired(Numerals.class, 0).addRequired(Arithmetic.class, 0).push();
		}
		if (this.pass == 1)
		{
			(new BranchActive(this)).addRequired(Ratio.class, 0).addRequired(Arithmetic.class, 1).push();
		}
		if (this.pass == 2)
		{
			(new BranchActive(this)).addRequired(Ratio.class, 1).addRequired(Zero.class, 0).push();
		}
	}

	public static int numPass()
	{
		return 3;
	}

	@Override
	public String getName()
	{
		switch (this.pass)
		{
			case 0:
				return "Ratios";
			case 1:
				return "Fractions";
			case 2:
				return "Decimals";
			default:
				return "Fractions";
		}
	}
}
