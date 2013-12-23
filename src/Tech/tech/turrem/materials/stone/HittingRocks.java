package tech.turrem.materials.stone;

import tech.turrem.tools.StoneTools;
import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.BranchSpontaneous;

public class HittingRocks extends TechBase
{
	public HittingRocks(int pass)
	{
		super(pass);
	}

	@Override
	public String getName()
	{
		return this.pass == 0 ? "Banging Rocks Together" : "Shaping Rocks";
	}

	public static int numPass()
	{
		return 2;
	}

	@Override
	public void loadBranches()
	{
		if (this.pass == 1)
		{
			(new BranchSpontaneous(this)).addRequired(StoneTools.class, 0).push();
		}
	}
	
	@Override
	public boolean isEntryLevel()
	{
		return this.pass == 0;
	}
}
