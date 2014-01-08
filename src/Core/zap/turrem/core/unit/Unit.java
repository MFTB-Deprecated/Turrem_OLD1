package zap.turrem.core.unit;

import zap.turrem.core.tech.item.TechItem;
import zap.turrem.core.unit.tech.TechUnit;

public abstract class Unit
{
	private int techItem = -1;
	
	public abstract String getName();
	
	public abstract boolean isStarting();
	
	public TechItem[] getRequiredTechs()
	{
		return null;
	}
	
	public void pushTechItem()
	{
		TechUnit tech = new TechUnit(this);
		this.techItem = tech.push();
	}

	public int getTheTech()
	{
		return techItem;
	}
}
