package zap.turrem.tech.branch;

import zap.turrem.tech.Tech;
import zap.turrem.tech.TechBase;

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

	public BranchSpontaneous(TechBase tech)
	{
		super(tech);
	}

	public BranchSpontaneous(Class<? extends Tech> tech, int pass)
	{
		super(tech, pass);
	}
}
