package tech.turrem.writing;

import tech.turrem.language.WrittenLanguage;
import tech.turrem.materials.plant.Fiber;
import tech.turrem.materials.plant.Glue;
import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.BranchActive;
import zap.turrem.tech.branch.BranchAvailable;

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
			(new BranchAvailable(this)).addRequired(ClayTablet.class, 1).addRequired(Ink.class, 1).addRequired(WrittenLanguage.class, 1).addRequired(Fiber.class, 0).push();
		}
		if (this.pass == 1)
		{
			(new BranchAvailable(this)).addRequired(Paper.class, 0).addRequired(Fiber.class, 1).push();
		}
		if (this.pass == 2)
		{
			(new BranchActive(this)).addRequired(Paper.class, 1).addRequired(Fiber.class, 2).push();
		}
		if (this.pass == 3)
		{
			(new BranchActive(this)).addRequired(Paper.class, 2).push();
		}
		if (this.pass == 4)
		{
			(new BranchAvailable(this)).addRequired(Paper.class, 3).addRequired(Glue.class, 2).push();
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
