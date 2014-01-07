package tech.turrem.materials.stone;

import tech.turrem.materials.metallurgy.metals.Iron;
import branch.turrem.BranchActive;
import zap.turrem.tech.TechBase;

public class Flint extends TechBase
{
	@Override
	public String getName(int pass)
	{
		return pass == 0 ? "Flint" : "Flint and Steel";
	}

	@Override
	public void loadBranches(int pass)
	{
		if (pass == 0)
		{
			(new BranchActive(this, pass)).addRequired(HittingRocks.class, 0).push();
		}
		if (pass == 1)
		{
			(new BranchActive(this, pass)).addRequired(Flint.class, 0).addRequired(Iron.class, 1).push();
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
