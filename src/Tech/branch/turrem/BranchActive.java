package branch.turrem;

import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.Branch;

public class BranchActive extends Branch
{
	public BranchActive(int tech)
	{
		super(tech);
	}

	public BranchActive(String tech)
	{
		super(tech);
	}

	public BranchActive(TechBase tech)
	{
		super(tech);
	}

	public BranchActive(Class<? extends TechBase> tech, int pass)
	{
		super(tech, pass);
	}
}
