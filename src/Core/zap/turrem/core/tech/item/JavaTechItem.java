package zap.turrem.core.tech.item;

import zap.turrem.core.tech.Tech;
import zap.turrem.core.tech.TechBase;

/**
 * 
 * @author Sam Sartor
 *
 */
public class JavaTechItem extends TechItem
{
	private Tech tech;
	private int pass;

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
