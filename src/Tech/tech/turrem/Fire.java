package tech.turrem;

import branch.turrem.BranchActive;
import branch.turrem.BranchSpontaneous;
import tech.turrem.materials.stone.Flint;
import zap.turrem.tech.TechBase;

public class Fire extends TechBase
{
	@Override
	public boolean isStarting(int pass)
	{
		return false;
	}

	@Override
	public void loadBraches(int pass)
	{
		if (pass == 0)
		{
			(new BranchSpontaneous(this, pass)).addRequired(Flint.class, 0).push();
			(new BranchSpontaneous(this, pass)).addRequired(Flint.class, 1).push();
		}
		if (pass == 1)
		{
			(new BranchActive(this, pass)).addRequired(Fire.class, 0).push();
			(new BranchSpontaneous(this, pass)).addRequired(Flint.class, 1).push();
		}
		
	}

	@Override
	public int getPassCount()
	{
		return 2;
	}

	@Override
	public String getName(int pass)
	{
		return pass == 0 ? "Sparks" : "Fire";
	}
}
