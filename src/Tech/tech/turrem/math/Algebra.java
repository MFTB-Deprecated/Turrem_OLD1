package tech.turrem.math;

import branch.turrem.BranchAvailable;

public class Algebra extends MathmaticalTech
{
	public Algebra(int pass)
	{
		super(pass);
	}

	@Override
	public void loadBranches()
	{
		(new BranchAvailable(this)).addRequired(Arithmetic.class, 2).addRequired(Arithmetic.class, 1).push();
	}

	@Override
	public String getName()
	{
		return "Algebra";
	}

}
