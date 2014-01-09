package tech.turrem.language;

import zap.turrem.core.tech.TechBase;
import branch.turrem.BranchActive;
import branch.turrem.BranchAvailable;

public class WrittenLanguage extends TechBase
{
	@Override
	public void loadBranches(int pass)
	{
		if (pass == 0)
		{
			(new BranchActive(this, pass)).addRequired(SpokenLanguage.class, 1).push();
		}
		if (pass == 1)
		{
			(new BranchActive(this, pass)).addRequired(WrittenLanguage.class, 0).push();
		}
		if (pass == 2)
		{
			(new BranchAvailable(this, pass)).addRequired(WrittenLanguage.class, 1).push();
		}
		if (pass == 3)
		{
			(new BranchAvailable(this, pass)).addRequired(WrittenLanguage.class, 2).push();
		}
		if (pass == 4)
		{
			(new BranchAvailable(this, pass)).addRequired(WrittenLanguage.class, 3).push();
		}
	}

	@Override
	public String getName(int pass)
	{
		switch (pass)
		{
			case 0:
				return "Pictograms";
			case 1:
				return "Ideograms";
			case 2:
				return "Phonemes";
			case 3:
				return "Alphabet";
			case 4:
				return "Script";
			default:
				return "Written Language";
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
		return 5;
	}
}
