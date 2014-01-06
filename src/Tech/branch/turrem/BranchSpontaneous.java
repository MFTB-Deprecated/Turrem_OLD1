package branch.turrem;

import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.Branch;
import zap.turrem.tech.item.TechItem;

public class BranchSpontaneous extends Branch
{
	public BranchSpontaneous(int tech)
	{
		super(tech);
	}

	public BranchSpontaneous(String tech)
	{
		super(tech);
	}
	
	public BranchSpontaneous(TechItem tech)
	{
		super(tech);
	}

	public BranchSpontaneous(Class<? extends TechBase> tech, int pass)
	{
		super(tech, pass);
	}
	
	public BranchSpontaneous(TechBase tech, int pass)
	{
		super(tech, pass);
	}
}
