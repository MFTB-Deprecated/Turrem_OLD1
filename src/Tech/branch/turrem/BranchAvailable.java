package branch.turrem;

import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.Branch;

public class BranchAvailable extends Branch
{
	public BranchAvailable(int tech)
	{
		super(tech);
	}

	public BranchAvailable(String tech)
	{
		super(tech);
	}

	public BranchAvailable(TechBase tech)
	{
		super(tech);
	}

	public BranchAvailable(Class<? extends TechBase> tech, int pass)
	{
		super(tech, pass);
	}
}
