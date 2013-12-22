package zap.turrem.tech.branch;

import zap.turrem.tech.TechBase;
import zap.turrem.tech.TechList;

public abstract class Branch
{
	protected int tech;
	
	protected int[] needed;
	
	public Branch(int tech, int... neededtech)
	{
		this.tech = tech;
		this.needed = neededtech;
	}

	public int getTechIndex()
	{
		return tech;
	}
	
	public String getTechIdentifier()
	{
		return TechList.getIdentifier(this.getTechIndex());
	}
	
	public TechBase getTech()
	{
		return TechList.getTech(this.getTechIndex());
	}
}
