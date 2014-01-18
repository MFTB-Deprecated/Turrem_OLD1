package tech.turrem.materials.stone;

import branch.turrem.BranchSpontaneous;
import tech.turrem.tools.StoneTools;
import zap.turrem.core.tech.TechBase;

public class HittingRocks extends TechBase
{
	@Override
	public String getName(int pass)
	{
		return pass == 0 ? "Rock Pounding" : "Rock Sculpting";
	}

	@Override
	public void loadBranches(int pass)
	{
		if (pass == 1)
		{
			(new BranchSpontaneous(this, pass)).addRequired(StoneTools.class, 0).push();
		}
	}

	@Override
	public boolean isEntryLevel(int pass)
	{
		return pass == 0;
	}

	@Override
	public int getPassCount()
	{
		return 2;
	}
}
