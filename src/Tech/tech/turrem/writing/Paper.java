package tech.turrem.writing;

import tech.turrem.materials.plant.Fiber;
import tech.turrem.materials.plant.Glue;
import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.BranchAvailable;
import zap.turrem.tech.branch.BranchSudden;

public class Paper extends TechBase
{
	public Paper(int pass)
	{
		super(pass);
	}
	
	@Override
	public void loadBranches()
	{
		if (this.pass == 0)
		{
			this.addBranch(new BranchAvailable(this.getIndex(Paper.class, 1), this.getIndex(Fiber.class, 1)));
		}
		if (this.pass == 1)
		{
			this.addBranch(new BranchSudden(this.getIndex(Paper.class, 2), this.getIndex(Fiber.class, 2)));
		}
		if (this.pass == 2)
		{
			this.addBranch(new BranchSudden(this.getIndex(Paper.class, 3)));
		}
		if (this.pass == 3)
		{
			this.addBranch(new BranchSudden(this.getIndex(Paper.class, 4), this.getIndex(Glue.class, 2)));
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
				return "Papyrus";
			case 1:
				return "Parchment";
			case 2:
				return "Paper";
			case 3:
				return "Scrolls";
			case 4:
				return "Books";
			default:
				return "Paper";
		}
	}
}
