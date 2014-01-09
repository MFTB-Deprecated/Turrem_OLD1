package tech.turrem.astronomy;

import zap.turrem.core.tech.TechBase;
import branch.turrem.BranchAvailable;

public class Astrolabe extends TechBase
{
	@Override
	public void loadBranches(int pass)
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
				return "Planispheres";
			case 1:
				return "Astrolabes";
			default:
				return "Astrolabes";
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
		return 2;
	}
}
