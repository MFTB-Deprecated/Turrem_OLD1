package tech.turrem.tools;

import tech.turrem.materials.stone.Flint;
import tech.turrem.materials.stone.HittingRocks;
import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.BranchAvailable;

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
			(new BranchAvailable(this)).addRequired(HittingRocks.class, 0).push();
		}
		if (this.pass == 1)
		{
			(new BranchAvailable(this)).addRequired(HittingRocks.class, 1).push();
		}
		if (this.pass == 2)
		{
			(new BranchAvailable(this)).addRequired(StoneTools.class, 1).addRequired(Flint.class, 0).push();
		}
	}
}
