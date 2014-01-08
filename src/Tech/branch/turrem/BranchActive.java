package branch.turrem;

import zap.turrem.core.tech.TechBase;
import zap.turrem.core.tech.branch.Branch;
import zap.turrem.core.tech.item.TechItem;

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
	
	public BranchActive(TechItem tech)
	{
		super(tech);
	}

	public BranchActive(Class<? extends TechBase> tech, int pass)
	{
		super(tech, pass);
	}
	
	public BranchActive(TechBase tech, int pass)
	{
		super(tech, pass);
	}
}
