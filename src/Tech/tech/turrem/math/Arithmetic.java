package tech.turrem.math;

import branch.turrem.BranchAvailable;

public class Arithmetic extends MathmaticalTech
{
	public Arithmetic(int pass)
	{
		super(pass);
	}

	@Override
	public void loadBranches()
	{
		if (this.pass == 0)
		{
			(new BranchAvailable(this)).addRequired(Numerals.class, 0).push();
		}
		if (this.pass == 1)
		{
			(new BranchAvailable(this)).addRequired(Arithmetic.class, 0).addRequired(Zero.class, 0).push();
		}
		if (this.pass == 2)
		{
			(new BranchAvailable(this)).addRequired(Arithmetic.class, 0).addRequired(Ratio.class, 1).push();
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
				return "Whole-Number Arithmetic";
			case 1:
				return "Integer Arithmetic";
			case 2:
				return "Arithmetic";
			default:
				return "Arithmetic";
		}
	}

}
