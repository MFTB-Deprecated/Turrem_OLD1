package zap.turrem.tech;

import zap.turrem.tech.branch.BranchAvailable;

public abstract class TechBase extends Tech
{
	public TechBase(int pass)
	{
		super(pass);
	}

	public abstract void loadBranches();

	public void loadAllBranches()
	{
		if (this.isEntryLevel())
		{
			(new BranchAvailable(this)).push();
		}
		this.loadBranches();
	}

	public final int getId()
	{
		return TechList.list.getIndex(this);
	}

	public abstract String getName();

	protected final int getIndex(Class<? extends TechBase> tech, int pass)
	{
		return TechList.list.getIndex(tech, pass);
	}

	public boolean isEntryLevel()
	{
		return false;
	}
}
