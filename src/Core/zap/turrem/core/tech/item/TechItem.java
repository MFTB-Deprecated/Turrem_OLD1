package zap.turrem.core.tech.item;

import zap.turrem.core.tech.list.TechList;

/**
 * Abstract class for storing specific, non-static techs
 */
public abstract class TechItem
{
	private boolean pushed = false;

	/**
	 * Gets the tech's string identifier
	 * 
	 * @return String identifier
	 */
	public abstract String getIdentifier();

	/**
	 * Gets the human-readable name of the tech
	 * 
	 * @return Name
	 */
	public abstract String getName();

	/**
	 * Call to load the tech's branches
	 */
	public abstract void loadBranches();

	/**
	 * Called when this tech is registered
	 */
	protected void onPush()
	{

	}

	/**
	 * Pushes the tech to the tech registry
	 */
	public final void push()
	{
		TechList.addTech(this);
		this.onPush();
		this.pushed = true;
	}

	/**
	 * Gets the index of this tech in the tech registry
	 * 
	 * @return Tech index
	 */
	public final int getId()
	{
		if (this.pushed)
		{
			return TechList.getIndex(this);
		}
		else
		{
			return -1;
		}
	}
}
