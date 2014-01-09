package zap.turrem.core.tech.branch;

import zap.turrem.core.tech.TechBase;
import zap.turrem.core.tech.item.TechItem;

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

	public BranchStarting(TechItem tech)
	{
		super(tech);
	}

	public BranchStarting(Class<? extends TechBase> tech, int pass)
	{
		super(tech, pass);
	}

	public BranchStarting(TechBase tech, int pass)
	{
		super(tech, pass);
	}
}