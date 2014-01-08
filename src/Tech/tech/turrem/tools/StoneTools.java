package tech.turrem.tools;

import branch.turrem.BranchAvailable;
import tech.turrem.materials.stone.Flint;
import tech.turrem.materials.stone.HittingRocks;
import zap.turrem.core.tech.TechBase;

public class StoneTools extends TechBase
{
	@Override
	public String getName(int pass)
	{
		switch (pass)
		{
			case 0:
				return "Stones as Tools";
			case 1:
				return "Stone Tools";
			case 2:
				return "Sharp Stone Tools";
			default:
				return "Stone Tools";
		}
	}

	@Override
	public void loadBranches(int pass)
	{
		if (pass == 0)
		{
			(new BranchAvailable(this, pass)).addRequired(HittingRocks.class, 0).push();
		}
		if (pass == 1)
		{
			(new BranchAvailable(this, pass)).addRequired(HittingRocks.class, 1).push();
		}
		if (pass == 2)
		{
			(new BranchAvailable(this, pass)).addRequired(StoneTools.class, 1).addRequired(Flint.class, 0).push();
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
