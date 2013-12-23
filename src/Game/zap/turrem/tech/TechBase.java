package zap.turrem.tech;

import zap.turrem.tech.branch.Branch;

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

	protected Branch[] branches = new Branch[0];

	public TechBase(int pass)
	{
		this.pass = pass;
	}

	public abstract void loadBranches();

	public final int getId()
	{
		return TechList.getIndex(this);
	}

	public final String getIdentifier()
	{
		return this.getIdentifier(this.pass);
	}

	public abstract String getName();

	protected final void addBranch(Branch branch)
	{
		Branch[] bs = new Branch[this.branches.length + 1];
		bs[0] = branch;
		for (int i = 0; i < this.branches.length; i++)
		{
			bs[i + 1] = this.branches[i];
		}
		this.branches = bs;
	}

	protected final int getIndex(Class<? extends TechBase> tech, int pass)
	{
		return TechList.getIndex(tech, pass);
	}
	
	public boolean isEntryLevel()
	{
		return false;
	}
}
