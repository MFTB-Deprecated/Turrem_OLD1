package zap.turrem.tech;

import zap.turrem.tech.branch.BranchAvailable;

/**
 * Make sure that if you extend this, you have a constructor with a single int
 * for parameters
 * 
 * @author Sam Sartor
 */
public abstract class TechBase extends Tech
{
	// TODO Make tech require resources and unlock things

	protected int pass;
	
	public TechBase(int pass)
	{
		this.pass = pass;
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
		return TechList.getIndex(this);
	}

	public final String getIdentifier()
	{
		return this.getIdentifier(this.pass);
	}

	public abstract String getName();

	protected final int getIndex(Class<? extends TechBase> tech, int pass)
	{
		return TechList.getIndex(tech, pass);
	}
	
	public boolean isEntryLevel()
	{
		return false;
	}
}
