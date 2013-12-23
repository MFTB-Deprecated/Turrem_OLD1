package tech.turrem.materials.plant;

import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.BranchAvailable;
import zap.turrem.tech.branch.BranchSpontaneous;

public class Fiber extends TechBase
{
	public Fiber(int pass)
	{
		super(pass);
	}

	@Override
	public void loadBranches()
	{
		if (this.pass == 1)
		{
			(new BranchAvailable(this)).addRequired(RawWood.class, 0).push();
		}
		if (this.pass == 2)
		{
			(new BranchSpontaneous(this)).addRequired(Fiber.class, 1).push();
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
				return "Reeds";
			case 1:
				return "Pulp";
			case 2:
				return "Plant Fiber";
			default:
				return "Paper";
		}
	}
	
	@Override
	public boolean isEntryLevel()
	{
		return this.pass == 0;
	}
}
