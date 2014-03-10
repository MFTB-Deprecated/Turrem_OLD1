package zap.turrem.core.entity;

import zap.turrem.utils.geo.Box;

public abstract class EntityUnit extends Entity
{
	protected int number;
	
	public EntityUnit()
	{
		super();
		this.number = this.defaultNumber();
		this.buildGrouping();
	}
	
	@Override
	protected void renderEntity()
	{
	}
	
	public abstract void renderSingle();
	
	public abstract Box singleBox();
	
	protected abstract int defaultNumber();
	
	protected abstract int addHowMany(int numtoadd);
	
	public int addIndividuals(int numtoadd)
	{
		int num = this.addHowMany(numtoadd);
		this.number += num;
		this.buildGrouping();
		return num;
	}
	
	public void buildGrouping()
	{
		
	}
}
