package tech.turrem.materials.plant;

import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.BranchAvailable;

public class RawWood extends TechBase
{
	public RawWood(int pass)
	{
		super(pass);
	}

	@Override
	public void loadBranches()
	{
		this.addBranch(new BranchAvailable(this.getIndex(Fiber.class, 1), this.getIndex(Fiber.class, 0)));
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
}
