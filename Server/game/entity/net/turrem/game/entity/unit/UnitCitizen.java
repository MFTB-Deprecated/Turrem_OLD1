package net.turrem.game.entity.unit;

import net.turrem.server.Realm;
import net.turrem.server.entity.unit.EntityUnit;
import net.turrem.server.load.control.GameEntity;
import net.turrem.server.load.control.SubscribeStartingEntity;
import net.turrem.server.world.World;
import net.turrem.utils.geo.Point;
import net.turrem.utils.nbt.NBTCompound;

@GameEntity(from = "turrem", author = "eekysam")
public class UnitCitizen extends EntityUnit
{
	@Override
	public void onEnter()
	{
	}

	@Override
	public void onTick()
	{
		super.onTick();
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
	
	@SubscribeStartingEntity
	public static UnitCitizen startingUnits(Realm realm, World world)
	{
		UnitCitizen cit = new UnitCitizen();
		cit.setAllegiance(realm);
		Point start = realm.startingLocation;
		cit.x = start.xCoord;
		cit.y = start.yCoord;
		cit.z = start.zCoord;
		world.addEntity(cit);
		return cit;
	}

	@Override
	public void writeNBT(NBTCompound data)
	{
	}
}
