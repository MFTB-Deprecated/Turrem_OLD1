package zap.turrem.tech.branch;

import zap.turrem.tech.Tech;
import zap.turrem.tech.TechBase;

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

	public BranchAvailable(Class<? extends Tech> tech, int pass)
	{
		super(tech, pass);
	}
}
