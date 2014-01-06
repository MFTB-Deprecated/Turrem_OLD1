package zap.turrem.tech;

import zap.turrem.tech.branch.BranchStarting;

public abstract class TechBase extends Tech
{
	public abstract boolean isStarting(int pass);
	
	public abstract void loadBraches(int pass);
	
	public void loadAllBranches(int pass)
	{
		this.loadBraches(pass);
		if (this.isStarting(pass))
		{
			(new BranchStarting(this.getClass(), pass)).push();
		}
	}
}
