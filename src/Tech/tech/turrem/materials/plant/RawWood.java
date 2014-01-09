package tech.turrem.materials.plant;

import zap.turrem.core.tech.TechBase;
import branch.turrem.BranchActive;

public class RawWood extends TechBase
{
	@Override
	public void loadBranches(int pass)
	{
		if (pass == 1)
		{
			(new BranchActive(this, pass)).addRequired(RawWood.class, 0).push();
		}
		if (pass == 2)
		{
			(new BranchActive(this, pass)).addRequired(RawWood.class, 1).push();
		}
	}

	@Override
	public String getName(int pass)
	{
		switch (pass)
		{
			case 0:
				return "Logs";
			case 1:
				return "Planks";
			case 2:
				return "Manufactured Boards";
			default:
				return "Wood";
		}
	}

	@Override
	public boolean isEntryLevel(int pass)
	{
		return pass == 0;
	}

	@Override
	public int getPassCount()
	{
		return 3;
	}
}
