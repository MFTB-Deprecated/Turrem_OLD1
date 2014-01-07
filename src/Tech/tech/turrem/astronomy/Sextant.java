package tech.turrem.astronomy;

import branch.turrem.BranchAvailable;
import zap.turrem.tech.TechBase;

public class Sextant extends TechBase
{
	@Override
	public void loadBranches(int pass)
	{
		if (pass == 1)
		{
			(new BranchAvailable(this, pass)).addRequired(Sextant.class, 0).push();
		}
		if (pass == 2)
		{
			(new BranchAvailable(this, pass)).addRequired(Sextant.class, 1).push();
		}
		if (pass == 3)
		{
			(new BranchAvailable(this, pass)).addRequired(Sextant.class, 1).addRequired(Telescope.class, 0).push();
		}
	}

	@Override
	public String getName(int pass)
	{
		switch (pass)
		{
			case 0:
				return "Mural Instrument";
			case 1:
				return "Dioptra";
			case 2:
				return "Sextant";
			case 3:
				return "Navigational Sextant";
			default:
				return "Sextant";
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
		return 4;
	}
}
