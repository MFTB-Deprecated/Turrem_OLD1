package tech.turrem.materials.plant;

import tech.turrem.writing.Paper;
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
		if (this.pass == 0)
		{
			this.addBranch(new BranchAvailable(this.getIndex(Paper.class, 0)));
		}
		if (this.pass == 1)
		{
			this.addBranch(new BranchSpontaneous(this.getIndex(Fiber.class, 2)));
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
}
