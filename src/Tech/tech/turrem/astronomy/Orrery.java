package tech.turrem.astronomy;

import branch.turrem.BranchAvailable;
import zap.turrem.tech.TechBase;

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
				return "Armillary sphere";
			case 1:
				return "Astrarium";
			case 2:
				return "Orrery";
			default:
				return "Orrery";
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
