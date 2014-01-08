package tech.turrem.astronomy;

import branch.turrem.BranchAvailable;
import zap.turrem.core.tech.TechBase;

public class Orrery extends TechBase
{
	@Override
	public void loadBranches(int pass)
	{
		if (pass == 2)
		{
			(new BranchAvailable(this, pass)).addRequired(Orrery.class, 1).push();
		}
	}
	
	@Override
	public String getName(int pass)
	{
		switch (pass)
		{
			case 0:
				return "Armillary Spheres";
			case 1:
				return "Mechanical Astrariums";
			case 2:
				return "Orreries";
			default:
				return "Orreries";
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
