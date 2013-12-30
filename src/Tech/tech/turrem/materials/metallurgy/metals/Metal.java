package tech.turrem.materials.metallurgy.metals;

import tech.turrem.materials.metallurgy.Metallurgy;
import branch.turrem.BranchActive;
import branch.turrem.BranchAvailable;
import zap.turrem.tech.TechBase;

public abstract class Metal extends TechBase
{
	public Metal(int pass)
	{
		super(pass);
	}

	@Override
	public void loadBranches()
	{
		if (this.pass == 1)
		{
			if (!this.isAlloy())
			{
				if (this.getLevel() == 0)
				{
					(new BranchActive(this)).addRequired(Metallurgy.class, 0).addRequired(this.getClass(), 0).push();
				}
				else if (this.getLevel() == 1)
				{
					(new BranchAvailable(this)).addRequired(Metallurgy.class, 1).addRequired(this.getClass(), 0).push();
				}
				else if (this.getLevel() == 2)
				{
					(new BranchAvailable(this)).addRequired(Metallurgy.class, 3).addRequired(this.getClass(), 0).push();
				}
			}
		}
		if (this.pass == 2)
		{
			(new BranchAvailable(this)).addRequired(Metallurgy.class, 4).addRequired(this.getClass(), 1).push();
		}
	}

	@Override
	public String getName()
	{
		switch (this.pass)
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

	public abstract String getMetalName();

	public static int numPass()
	{
		return 3;
	}

	public abstract int getLevel();

	public boolean isAlloy()
	{
		return false;
	}
}
