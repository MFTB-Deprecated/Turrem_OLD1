package tech.turrem;

import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.BranchSpontaneous;

public class Flint extends TechBase
{
	public Flint(int pass)
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
		return this.pass == 0 ? "Flint and a Rock" : "Flint and Steel";
	}

	@Override
	public void loadBranches()
	{
		if (this.pass == 0)
		{
			this.addBranch(new BranchSpontaneous(this.getIndex(Fire.class, 0)));
		}
		if (this.pass == 1)
		{
			this.addBranch(new BranchSpontaneous(this.getIndex(Fire.class, 0)));
			this.addBranch(new BranchSpontaneous(this.getIndex(Fire.class, 1)));
		}
	}
}
