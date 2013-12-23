package tech.turrem.language;

import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.BranchSudden;

public class SpokenLanguage extends TechBase
{
	public SpokenLanguage(int pass)
	{
		super(pass);
	}

	@Override
	public void loadBranches()
	{
		if (this.pass == 0)
		{
			this.addBranch(new BranchSudden(this.getIndex(SpokenLanguage.class, 1)));
		}
		if (this.pass == 1)
		{
			this.addBranch(new BranchSudden(this.getIndex(WrittenLanguage.class, 0)));
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
		return true;
	}
}
