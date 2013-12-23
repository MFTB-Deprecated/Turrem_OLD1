package tech.turrem.math;

import zap.turrem.tech.branch.BranchAvailable;

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
			this.addBranch(new BranchAvailable(this.getIndex(Arithmetic.class, 1), this.getIndex(Zero.class, 0)));
			this.addBranch(new BranchAvailable(this.getIndex(Arithmetic.class, 2), this.getIndex(Ratio.class, 1)));
		}
		if (this.pass == 2)
		{
			this.addBranch(new BranchAvailable(this.getIndex(Geometry.class, 0)));
			this.addBranch(new BranchAvailable(this.getIndex(Algebra.class, 0)));
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
