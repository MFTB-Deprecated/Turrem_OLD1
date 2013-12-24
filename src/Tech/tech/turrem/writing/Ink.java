package tech.turrem.writing;

import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.BranchAvailable;

public class Ink extends TechBase
{
	public Ink(int pass)
	{
		super(pass);
	}

	@Override
	public void loadBranches()
	{
		if (this.pass == 1)
		{
			(new BranchAvailable(this)).addRequired(Paper.class, 0).addRequired(Ink.class, 0).push();
		}
		if (this.pass == 2)
		{
			(new BranchAvailable(this)).addRequired(Paper.class, 0).addRequired(Ink.class, 0).push();
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
	public boolean isEntryLevel()
	{
		return this.pass == 0;
	}
}
