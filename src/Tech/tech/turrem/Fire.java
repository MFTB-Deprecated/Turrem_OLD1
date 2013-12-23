package tech.turrem;

import tech.turrem.materials.stone.Flint;
import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.BranchActive;
import zap.turrem.tech.branch.BranchSpontaneous;

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
			(new BranchSpontaneous(this)).addRequired(Flint.class, 0).push();
			(new BranchSpontaneous(this)).addRequired(Flint.class, 1).push();
		}
		if (this.pass == 1)
		{
			(new BranchActive(this)).addRequired(Fire.class, 0).push();
			(new BranchSpontaneous(this)).addRequired(Flint.class, 1).push();
		}
	}
}
