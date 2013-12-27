package tech.turrem.language;

import branch.turrem.BranchActive;
import branch.turrem.BranchAvailable;
import zap.turrem.tech.TechBase;

public class SpokenLanguage extends TechBase
{
	public SpokenLanguage(int pass)
	{
		super(pass);
	}

	@Override
	public void loadBranches()
	{
		if (this.pass == 1)
		{
			(new BranchActive(this)).addRequired(SpokenLanguage.class, 0).push();
		}
		if (this.pass == 2)
		{
			(new BranchAvailable(this)).addRequired(WrittenLanguage.class, 1).addRequired(SpokenLanguage.class, 1).push();
		}
	}

	public static int numPass()
	{
		return 3;
	}

	@Override
	public String getName()
	{
		switch (this.pass)
		{
			case 0:
				return "Words";
			case 1:
				return "Language";
			case 2:
				return "Advanced Language";
			default:
				return "Language";
		}
	}

	@Override
	public boolean isEntryLevel()
	{
		return this.pass == 0;
	}
}
