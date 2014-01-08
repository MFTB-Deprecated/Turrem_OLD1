package zap.turrem.core.tech;

import zap.turrem.core.tech.branch.BranchStarting;

public abstract class TechBase extends Tech
{
	public abstract boolean isEntryLevel(int pass);
	
	public abstract void loadBranches(int pass);
	
	public void loadAllBranches(int pass)
	{
		this.loadBranches(pass);
		if (this.isEntryLevel(pass))
		{
			(new BranchStarting(this.getClass(), pass)).push();
		}
	}
}
