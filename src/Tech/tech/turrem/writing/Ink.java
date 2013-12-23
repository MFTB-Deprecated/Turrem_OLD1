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
		if (this.pass == 0)
		{
			this.addBranch(new BranchAvailable(this.getIndex(Ink.class, 1), this.getIndex(Paper.class, 0)));
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
}
