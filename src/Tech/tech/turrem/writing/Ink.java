package tech.turrem.writing;

import branch.turrem.BranchAvailable;
import zap.turrem.tech.TechBase;

public class Ink extends TechBase
{
	@Override
	public void loadBranches(int pass)
	{
		if (pass == 1)
		{
			(new BranchAvailable(this, pass)).addRequired(Ink.class, 0).push();
		}
		if (pass == 2)
		{
			(new BranchAvailable(this, pass)).addRequired(Paper.class, 0).addRequired(Ink.class, 0).push();
		}
	}

	@Override
	public String getName(int pass)
	{
		switch (pass)
		{
			case 0:
				return "Dye";
			case 1:
				return "Ink";
			case 2:
				return "Colored Dye";
			default:
				return "Ink";
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
