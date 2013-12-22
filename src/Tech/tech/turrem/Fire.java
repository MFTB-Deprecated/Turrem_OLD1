package tech.turrem;

import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.BranchSudden;

public class Fire extends TechBase
{
	public Fire(int pass)
	{
		super(pass);
	}

	public static int numPass()
	{
		return 2;
	}

	@Override
	public String getName()
	{
		return this.pass == 0 ? "Sparks" : "Fire";
	}

	@Override
	public void loadBranches()
	{
		if (this.pass == 0)
		{
			this.addBranch(new BranchSudden(this.getIndex(Fire.class, 1)));
		}
	}
}
