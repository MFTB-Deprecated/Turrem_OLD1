package zap.turrem.tech.branch;

import zap.turrem.tech.TechBase;
import zap.turrem.tech.item.TechItem;

public class BranchUnitTech extends Branch
{
	public BranchUnitTech(int tech)
	{
		super(tech);
	}

	public BranchUnitTech(String tech)
	{
		super(tech);
	}
	
	public BranchUnitTech(TechItem tech)
	{
		super(tech);
	}

	public BranchUnitTech(Class<? extends TechBase> tech, int pass)
	{
		super(tech, pass);
	}
	
	public BranchUnitTech(TechBase tech, int pass)
	{
		super(tech, pass);
	}
}
