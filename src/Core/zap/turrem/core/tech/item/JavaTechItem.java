package zap.turrem.core.tech.item;

import zap.turrem.core.tech.Tech;
import zap.turrem.core.tech.TechBase;

/**
 * Implementation of TechItem for use with Turrem's java-based techs
 */
public class JavaTechItem extends TechItem
{
	private Tech tech;
	private int pass;

	/**
	 * Constructs a new tech item using the 'static' tech class and pass
	 * @param tech The class that hold the tech
	 * @param pass The pass to give the tech class
	 */
	public JavaTechItem(Tech tech, int pass)
	{
		this.tech = tech;
		this.pass = pass;
	}

	@Override
	public String getIdentifier()
	{
		return this.tech.getIdentifier(this.pass);
	}

	@Override
	public String getName()
	{
		return this.tech.getName(this.pass);
	}

	@Override
	public void loadBranches()
	{
		if (this.tech instanceof TechBase)
		{
			((TechBase) this.tech).loadAllBranches(this.pass);
		}
	}
}
