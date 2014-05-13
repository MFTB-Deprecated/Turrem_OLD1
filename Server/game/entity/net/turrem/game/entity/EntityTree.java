package net.turrem.game.entity;

import net.turrem.server.load.control.GameEntity;

@GameEntity(from = "turrem", author = "eekysam")
public class EntityTree extends EntityWorldDec
{
	@Override
	public String getEntityType()
	{
		return "tree";
	}
}