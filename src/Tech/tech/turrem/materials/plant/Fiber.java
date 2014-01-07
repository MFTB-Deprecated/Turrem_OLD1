package tech.turrem.materials.plant;

import branch.turrem.BranchAvailable;
import branch.turrem.BranchSpontaneous;
import zap.turrem.tech.TechBase;

public class Fiber extends TechBase
{
	@Override
	public void loadBranches(int pass)
	{
		if (pass == 1)
		{
			(new BranchAvailable(this, pass)).addRequired(RawWood.class, 0).push();
		}
		if (pass == 2)
		{
			(new BranchSpontaneous(this, pass)).addRequired(Fiber.class, 1).push();
		}
	}

	@Override
	public String getName(int pass)
	{
		switch (pass)
		{
			case 0:
				return "Reeds";
			case 1:
				return "Pulp";
			case 2:
				return "Plant Fiber";
			default:
				return "Paper";
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
