package tech.turrem.tools;

import tech.turrem.materials.stone.Flint;
import tech.turrem.materials.stone.HittingRocks;
import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.BranchAvailable;
import zap.turrem.tech.branch.BranchSpontaneous;

public class StoneTools extends TechBase
{
	public StoneTools(int pass)
	{
		super(pass);
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
				return "Stones";
			case 1:
				return "Stone Tools";
			case 2:
				return "Sharp Stone Tools";
			default:
				return "Stone Tools";
		}
	}

	@Override
	public void loadBranches()
	{
		if (this.pass == 0)
		{
			this.addBranch(new BranchSpontaneous(this.getIndex(HittingRocks.class, 1)));
		}
		if (this.pass == 1)
		{
			this.addBranch(new BranchAvailable(this.getIndex(StoneTools.class, 2), this.getIndex(Flint.class, 0)));
		}
	}
}
