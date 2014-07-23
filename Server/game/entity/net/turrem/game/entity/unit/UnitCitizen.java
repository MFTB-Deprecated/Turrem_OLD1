package net.turrem.game.entity.unit;

import net.turrem.server.Realm;
import net.turrem.server.entity.unit.EntityUnit;
import net.turrem.server.load.control.GameEntity;
import net.turrem.server.load.control.SubscribeStartingEntity;
import net.turrem.server.world.World;

@GameEntity(from = "turrem", author = "eekysam")
public class UnitCitizen extends EntityUnit
{	
	@SubscribeStartingEntity
	public static UnitCitizen startingUnits(Realm realm, World world)
	{
		return null;
	}

	@Override
	public boolean getRealmVisibility(int x, int z)
	{
		return false;
	}

	@Override
	public boolean getThisVisibility(int x, int z)
	{
		return false;
	}
}
