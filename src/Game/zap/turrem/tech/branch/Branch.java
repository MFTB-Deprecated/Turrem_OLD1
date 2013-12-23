package zap.turrem.tech.branch;

import zap.turrem.tech.Tech;
import zap.turrem.tech.TechBase;
import zap.turrem.tech.TechList;
import zap.turrem.tech.branch.data.BranchData;

public abstract class Branch
{
	public static int instanceCount = 0;
	
	protected int[] techs = new int[0];
	protected int[] requiredtechs = new int[0];
	
	private int id = -1;
	
	public Branch(int tech)
	{
		this.techs = new int[] {tech};
		instanceCount++;
	}
	
	public int getId()
	{
		return id;
	}

	public Branch(String tech)
	{
		this(TechList.getIndex(tech));
	}
	
	public Branch(TechBase tech)
	{
		this(TechList.getIndex(tech));
	}
	
	public Branch(Class<? extends Tech> tech, int pass)
	{
		this(TechList.getIndex(tech, pass));
	}
	
	public Branch addRequired(int tech)
	{
		int[] ts = new int[this.requiredtechs.length + 1];
		ts[0] = tech;
		for (int i = 0; i < this.requiredtechs.length; i++)
		{
			ts[i + 1] = this.requiredtechs[i];
		}
		this.requiredtechs = ts;
		return this;
	}
	
	public Branch addRequired(String tech)
	{
		return this.addRequired(TechList.getIndex(tech));
	}
	
	public Branch addRequired(TechBase tech)
	{
		return this.addRequired(TechList.getIndex(tech));
	}
	
	public Branch addRequired(Class<? extends Tech> tech, int pass)
	{
		return this.addRequired(TechList.getIndex(tech, pass));
	}
	
	public void push()
	{
		this.id = BranchList.addBranch(this);
	}
	
	public abstract void onStart(BranchData data);
	
	public abstract boolean isDone(BranchData data);
	
	public abstract boolean onTick(BranchData data, int tickRate);
	
	public abstract void onDone(BranchData data);
}
