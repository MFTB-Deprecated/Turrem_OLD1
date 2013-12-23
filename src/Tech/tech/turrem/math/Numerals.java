package tech.turrem.math;

import zap.turrem.tech.branch.BranchAvailable;

public class Numerals extends MathmaticalTech
{
	public Numerals(int pass)
	{
		super(pass);
	}

	@Override
	public void loadBranches()
	{
		if (this.pass == 0)
		{
			this.addBranch(new BranchAvailable(this.getIndex(Arithmetic.class, 0)));
			this.addBranch(new BranchAvailable(this.getIndex(Zero.class, 0)));
		}
	}

	@Override
	public String getName()
	{
		return "Numerals";
	}
}
