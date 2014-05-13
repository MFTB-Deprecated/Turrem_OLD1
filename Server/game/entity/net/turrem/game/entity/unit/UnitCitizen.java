package net.turrem.game.entity.unit;

import net.turrem.server.entity.unit.EntityUnit;

public class UnitCitizen extends EntityUnit
{
	@Override
	public void onEnter()
	{
	}

	@Override
	public short loadRadius()
	{
		return 1;
	}

	@Override
	public float veiwDistance()
	{
		return 16.0F;
	}

	@Override
	public String getEntityType()
	{
		return "citizen";
	}
}
