package zap.turrem.core.tech.branch;

import zap.turrem.core.tech.TechBase;
import zap.turrem.core.tech.item.TechItem;
import zap.turrem.core.tech.list.TechList;

public abstract class Branch
{
	protected int[] techs = new int[0];
	protected int[] requiredtechs = new int[0];

	private int id = -1;

	public Branch(int tech)
	{
		this.techs = new int[] { tech };
	}

	public final int[] getTechs()
	{
		return techs;
	}

	public final int[] getRequiredTechs()
	{
		return requiredtechs;
	}

	public int getId()
	{
		return this.id;
	}

	public Branch(String tech)
	{
		this(TechList.getIndex(tech));
	}

	public Branch(TechItem tech)
	{
		this(tech.getId());
	}

	public Branch(Class<? extends TechBase> tech, int pass)
	{
		this(TechList.getIndex(tech, pass));
	}

	public Branch(TechBase tech, int pass)
	{
		this(tech.getClass(), pass);
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

	public Branch addRequired(Class<? extends TechBase> tech, int pass)
	{
		return this.addRequired(TechList.getIndex(tech, pass));
	}

	public void push()
	{
		this.id = BranchList.addBranch(this);
	}
}