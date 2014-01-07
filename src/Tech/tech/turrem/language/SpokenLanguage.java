package tech.turrem.language;

import branch.turrem.BranchActive;
import branch.turrem.BranchAvailable;
import zap.turrem.tech.TechBase;

public class SpokenLanguage extends TechBase
{
	@Override
	public void loadBranches(int pass)
	{
		if (pass == 1)
		{
			(new BranchActive(this, pass)).addRequired(SpokenLanguage.class, 0).push();
		}
		if (pass == 2)
		{
			(new BranchAvailable(this, pass)).addRequired(WrittenLanguage.class, 1).addRequired(SpokenLanguage.class, 1).push();
		}
	}

	@Override
	public String getName(int pass)
	{
		switch (pass)
		{
			case 0:
				return "Words";
			case 1:
				return "Simple Language";
			case 2:
				return "Advanced Language";
			default:
				return "Language";
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
