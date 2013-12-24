package tech.turrem.materials.plant;

import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.BranchActive;

public class RawWood extends TechBase
{
	public RawWood(int pass)
	{
		super(pass);
	}

	@Override
	public void loadBranches()
	{
		if (this.pass == 1)
		{
			(new BranchActive(this)).addRequired(RawWood.class, 0).push();
		}
		if (this.pass == 2)
		{
			(new BranchActive(this)).addRequired(RawWood.class, 1).push();
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
				return "Logs";
			case 1:
				return "Planks";
			case 2:
				return "Boards";
			default:
				return "Wood";
		}
	}

	@Override
	public boolean isEntryLevel()
	{
		return this.pass == 0;
	}
}
