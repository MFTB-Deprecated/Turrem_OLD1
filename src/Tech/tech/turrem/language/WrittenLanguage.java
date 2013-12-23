package tech.turrem.language;

import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.BranchActive;
import zap.turrem.tech.branch.BranchAvailable;

public class WrittenLanguage extends TechBase
{
	public WrittenLanguage(int pass)
	{
		super(pass);
	}

	@Override
	public void loadBranches()
	{
		if (this.pass == 0)
		{
			(new BranchActive(this)).addRequired(SpokenLanguage.class, 1).push();
		}
		if (this.pass == 1)
		{
			(new BranchActive(this)).addRequired(WrittenLanguage.class, 0).push();
		}
		if (this.pass == 2)
		{
			(new BranchAvailable(this)).addRequired(WrittenLanguage.class, 1).push();
		}
		if (this.pass == 3)
		{
			(new BranchAvailable(this)).addRequired(WrittenLanguage.class, 2).push();
		}
		if (this.pass == 4)
		{
			(new BranchAvailable(this)).addRequired(WrittenLanguage.class, 3).push();
		}
	}

	public static int numPass()
	{
		return 5;
	}

	@Override
	public String getName()
	{
		switch (this.pass)
		{
			case 0:
				return "Pictograms";
			case 1:
				return "Ideograms";
			case 2:
				return "Phoneme";
			case 3:
				return "Alphabet";
			case 4:
				return "Script";
			default:
				return "Written Language";
		}
	}
}
