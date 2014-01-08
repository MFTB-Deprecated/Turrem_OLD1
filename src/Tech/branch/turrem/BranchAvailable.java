package branch.turrem;

import zap.turrem.core.tech.TechBase;
import zap.turrem.core.tech.branch.Branch;
import zap.turrem.core.tech.item.TechItem;

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
	
	public BranchAvailable(TechItem tech)
	{
		super(tech);
	}

	public BranchAvailable(Class<? extends TechBase> tech, int pass)
	{
		super(tech, pass);
	}
	
	public BranchAvailable(TechBase tech, int pass)
	{
		super(tech, pass);
	}
}
