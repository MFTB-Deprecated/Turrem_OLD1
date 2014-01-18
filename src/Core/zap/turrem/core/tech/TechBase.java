package zap.turrem.core.tech;

import zap.turrem.core.tech.branch.BranchStarting;

/**
 * This class defines everything about a particular tech. This class may be
 * constructed for each use, so it must act in a static manner, with no feilds.
 * Can be used to define multiple techs, each with a different pass number,
 */
public abstract class TechBase extends Tech
{
	/**
	 * Does the player start out with branch access to this tech
	 * 
	 * @param pass The pass number, differentiates between techs that also use
	 *            this class
	 * @return Is the player have an entry point into the tech tree through this
	 *         tech
	 */
	public abstract boolean isEntryLevel(int pass);

	/**
	 * All further tech tree branches should be initialized from this method
	 * 
	 * @param pass The pass number, differentiates between techs that also use
	 *            this class
	 */
	public abstract void loadBranches(int pass);

	/**
	 * This methoud calls loadBranches() before doing the branch calculations
	 * common to all techs. This method should always be called instead of
	 * loadBranches
	 * 
	 * @param pass The pass number, differentiates between techs that also use
	 *            this class
	 */
	public void loadAllBranches(int pass)
	{
		this.loadBranches(pass);
		if (this.isEntryLevel(pass))
		{
			(new BranchStarting(this.getClass(), pass)).push();
		}
	}
}
