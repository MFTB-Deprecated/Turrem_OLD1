package zap.turrem.tech.item;

import zap.turrem.tech.TechBase;

public class JavaTechItem extends TechItem
{
	private TechBase tech;
	private int pass;
	
	public JavaTechItem(TechBase tech, int pass)
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
	public void loadBraches()
	{
		this.tech.loadAllBranches(this.pass);
	}
}
