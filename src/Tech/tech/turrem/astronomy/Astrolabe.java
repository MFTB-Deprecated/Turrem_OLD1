package tech.turrem.astronomy;

import branch.turrem.BranchAvailable;
import zap.turrem.tech.TechBase;

public class Astrolabe extends TechBase
{
	@Override
	public void loadBraches(int pass)
	{
		if (pass == 1)
		{
			(new BranchAvailable(this, pass)).addRequired(Astrolabe.class, 0).addRequired(Sextant.class, 1).push();
		}
	}

	@Override
	public String getName(int pass)
	{
		switch (pass)
		{
			case 0:
				return "Planisphere";
			case 1:
				return "Astrolabe";
			default:
				return "Astrolabe";
		}
	}

	@Override
	public boolean isStarting(int pass)
	{
		return false;
	}
	@Override
	public int getPassCount()
	{
		return 2;
	}
}
