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

	/**
	 * Gets the indexes of the techs this branch unlocks
	 * @return List of tech indexes (Array size always 1 for the time being)
	 */
	public final int[] getTechs()
	{
		return this.techs;
	}

	/**
	 * Gets the indexes of the techs this branch requires to unlock
	 * @return List of tech indexes
	 */
	public final int[] getRequiredTechs()
	{
		return this.requiredtechs;
	}

	/**
	 * Gets the index of this branch in the global branch registry
	 * @return This branch index (-1 if push() has not been called)
	 */
	public int getId()
	{
		return this.id;
	}

	/**
	 * Constructs a new branch with the tech that the branch unlocks
	 * @param tech Tech identifier
	 */
	public Branch(String tech)
	{
		this(TechList.getIndex(tech));
	}

	/**
	 * Constructs a new branch with the tech that the branch unlocks
	 * @param tech TechItem representation
	 */
	public Branch(TechItem tech)
	{
		this(tech.getId());
	}

	/**
	 * Constructs a new branch with the tech that the branch unlocks
	 * @param tech The tech's class
	 * @param pass The tech pass
	 */
	public Branch(Class<? extends TechBase> tech, int pass)
	{
		this(TechList.getIndex(tech, pass));
	}

	/**
	 * Constructs a new branch with the tech that the branch unlocks
	 * @param tech Static tech class object
	 * @param pass The tech pass
	 */
	public Branch(TechBase tech, int pass)
	{
		this(tech.getClass(), pass);
	}

	/**
	 * Adds a required tech to the branch
	 * @param tech Tech index
	 * @return This branch
	 */
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

	/**
	 * Adds a required tech to the branch
	 * @param tech Tech identifier
	 * @return This branch
	 */
	public Branch addRequired(String tech)
	{
		return this.addRequired(TechList.getIndex(tech));
	}

	/**
	 * Adds a required tech to the branch
	 * @param tech The tech's class
	 * @param pass The tech pass
	 * @return This branch
	 */
	public Branch addRequired(Class<? extends TechBase> tech, int pass)
	{
		return this.addRequired(TechList.getIndex(tech, pass));
	}

	/**
	 * Pushes this local branch object to the global branch registry
	 */
	public void push()
	{
		this.id = BranchList.addBranch(this);
	}
}