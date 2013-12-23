package tech.turrem.math;

import zap.turrem.tech.branch.BranchSudden;

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
			this.addBranch(new BranchSudden(this.getIndex(Ratio.class, 1)));
		}
		if (this.pass == 1)
		{
			this.addBranch(new BranchSudden(this.getIndex(Ratio.class, 2)));
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
