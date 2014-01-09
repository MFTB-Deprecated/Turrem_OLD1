package tech.turrem.materials.metallurgy.metals;

import tech.turrem.materials.metallurgy.Metallurgy;
import zap.turrem.core.tech.TechBase;
import branch.turrem.BranchActive;
import branch.turrem.BranchAvailable;

public abstract class Metal extends TechBase
{
	@Override
	public void loadBranches(int pass)
	{
		if (pass == 0)
		{
			(new BranchActive(this, pass)).addRequired(Metallurgy.class, 0).push();
		}
		if (pass == 1)
		{
			if (this.getLevel() == 0)
			{
				(new BranchActive(this, pass)).addRequired(Metallurgy.class, 0).addRequired(this.getClass(), 0).push();
			}
			else if (this.getLevel() == 1)
			{
				(new BranchAvailable(this, pass)).addRequired(Metallurgy.class, 1).addRequired(this.getClass(), 0).push();
			}
			else if (this.getLevel() == 2)
			{
				(new BranchAvailable(this, pass)).addRequired(Metallurgy.class, 3).addRequired(this.getClass(), 0).push();
			}
		}
		if (pass == 2)
		{
			(new BranchAvailable(this, pass)).addRequired(Metallurgy.class, 4).addRequired(this.getClass(), 1).push();
		}
	}

	@Override
	public String getName(int pass)
	{
		switch (pass)
		{
			case 0:
				if (this.isAlloy())
				{
					return this.getMetalName() + " Mixing";
				}
				else
				{
					return this.getMetalName() + " Ore";
				}
			case 1:
				return this.getMetalName();
			case 2:
				return this.getMetalName() + " Working";
			default:
				return this.getMetalName();
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
		return 3;
	}

	public abstract String getMetalName();

	public abstract int getLevel();

	public boolean isAlloy()
	{
		return false;
	}
}
