package zap.turrem.tech.branch;

import zap.turrem.tech.Tech;
import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.data.BranchData;

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
	
	@Override
	public void onStart(BranchData data)
	{
		
	}

	@Override
	public boolean isDone(BranchData data)
	{
		return false;
	}

	@Override
	public boolean onTick(BranchData data, int tickRate)
	{
		return false;
	}

	@Override
	public void onDone(BranchData data)
	{
		
	}
}
