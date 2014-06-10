package net.turrem.game.entity;

import net.turrem.server.entity.SolidEntity;
import net.turrem.server.load.control.GameEntity;

@GameEntity(from = "turrem", author = "eekysam")
public class EntityWorldDec extends SolidEntity
{
	@Override
	public String getEntityType()
	{
		return "worldDecor";
	}

	@Override
	public void onEnter()
	{
	}

	@Override
	public short loadRadius()
	{
		return -1;
	}

	@Override
	public float veiwDistance()
	{
		return 0.0F;
	}
}
