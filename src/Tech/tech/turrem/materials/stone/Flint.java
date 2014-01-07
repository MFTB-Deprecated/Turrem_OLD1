package tech.turrem.materials.stone;

import branch.turrem.BranchActive;
import zap.turrem.tech.TechBase;

public class Flint extends TechBase
{
	@Override
	public String getName(int pass)
	{
		return pass == 0 ? "Flint and a Rock" : "Flint and Steel";
	}

	@Override
	public void loadBranches(int pass)
	{
		if (pass == 0)
		{
			(new BranchActive(this, pass)).addRequired(HittingRocks.class, 0).push();
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
		return 2;
	}
}
