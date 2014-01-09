package tech.turrem.materials.metallurgy;

import tech.turrem.Fire;
import tech.turrem.tools.StoneTools;
import zap.turrem.core.tech.TechBase;
import branch.turrem.BranchAvailable;

public class Metallurgy extends TechBase
{
	@Override
	public void loadBranches(int pass)
	{
		if (pass == 0)
		{
			(new BranchAvailable(this, pass)).addRequired(StoneTools.class, 2).addRequired(Fire.class, 1).push();
		}
		if (pass > 0)
		{
			(new BranchAvailable(this, pass)).addRequired(this.getClass(), pass - 1).push();
		}
	}

	@Override
	public String getName(int pass)
	{
		switch (pass)
		{
			case 0:
				return "Metallic Rocks";
			case 1:
				return "Metallurgy";
			case 2:
				return "Alloys";
			case 3:
				return "Complex Metallurgy";
			case 4:
				return "Metal Working";
			default:
				return "Metallurgy";
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
