package tech.turrem;

import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.BranchAvailable;
import zap.turrem.tech.branch.BranchSudden;

public class HittingRocks extends TechBase
{
	public HittingRocks(int pass)
	{
		super(pass);
	}

	@Override
	public String getName()
	{
		return this.pass == 0 ? "Banging Rocks Together" : "Shaping Rocks";
	}

	public static int numPass()
	{
		return 2;
	}

	@Override
	public void loadBranches()
	{
		if (this.pass == 0)
		{
			this.addBranch(new BranchAvailable(this.getIndex(StoneTools.class, 0)));
			this.addBranch(new BranchSudden(this.getIndex(Flint.class, 0)));
		}
		if (this.pass == 1)
		{
			this.addBranch(new BranchAvailable(this.getIndex(StoneTools.class, 1)));
		}
	}
}
