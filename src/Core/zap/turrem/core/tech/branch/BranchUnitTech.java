package zap.turrem.core.tech.branch;

import zap.turrem.core.tech.TechBase;
import zap.turrem.core.tech.item.TechItem;

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

	@Override
	public boolean sudden()
	{
		return false;
	}

	@Override
	public boolean random()
	{
		return false;
	}
}
