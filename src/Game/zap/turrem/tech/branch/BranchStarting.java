package zap.turrem.tech.branch;

import zap.turrem.tech.TechBase;

public class BranchStarting extends Branch
{
	public BranchStarting(int tech)
	{
		super(tech);
	}

	public BranchStarting(String tech)
	{
		super(tech);
	}

	public BranchStarting(TechBase tech)
	{
		super(tech);
	}

	public BranchStarting(Class<? extends TechBase> tech, int pass)
	{
		super(tech, pass);
	}
}
