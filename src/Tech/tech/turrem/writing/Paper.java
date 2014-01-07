package tech.turrem.writing;

import branch.turrem.BranchActive;
import branch.turrem.BranchAvailable;
import tech.turrem.language.WrittenLanguage;
import tech.turrem.materials.plant.Fiber;
import tech.turrem.materials.plant.Glue;
import zap.turrem.tech.TechBase;

public class Paper extends TechBase
{
	@Override
	public void loadBranches(int pass)
	{
		if (pass == 0)
		{
			(new BranchAvailable(this, pass)).addRequired(ClayTablet.class, 1).addRequired(Ink.class, 1).addRequired(WrittenLanguage.class, 1).addRequired(Fiber.class, 0).push();
		}
		if (pass == 1)
		{
			(new BranchAvailable(this, pass)).addRequired(Paper.class, 0).addRequired(Fiber.class, 1).push();
		}
		if (pass == 2)
		{
			(new BranchActive(this, pass)).addRequired(Paper.class, 1).addRequired(Fiber.class, 2).push();
		}
		if (pass == 3)
		{
			(new BranchActive(this, pass)).addRequired(Paper.class, 2).push();
		}
		if (pass == 4)
		{
			(new BranchAvailable(this, pass)).addRequired(Paper.class, 3).addRequired(Glue.class, 2).push();
		}
	}
	
	@Override
	public String getName(int pass)
	{
		switch (pass)
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

	@Override
	public boolean isEntryLevel(int pass)
	{
		return false;
	}

	@Override
	public int getPassCount()
	{
		return 5;
	}
}
