package zap.turrem.server.realm;

import java.util.ArrayList;
import java.util.List;

import zap.turrem.core.player.Player;
import zap.turrem.core.realm.Realm;
import zap.turrem.server.entity.unit.Unit;
import zap.turrem.server.world.World;

public class RealmServer extends Realm
{
	protected World theWorld;
	protected int id;
	
	protected List<Integer> units = new ArrayList<Integer>();
	
	public RealmServer(Player thePlayer, World world)
	{
		super(thePlayer);
		this.theWorld = world;
		this.id = this.theWorld.addRealm(this);
	}

	public final int getId()
	{
		return id;
	}
	
	public void reUnitList()
	{
		List<Unit> ulist = this.theWorld.getUnitsOfRealm(this);
		this.units.clear();
		for (Unit unit : ulist)
		{
			this.units.add(unit.getId());
		}
	}
	
	public void addUnit(Unit unit)
	{
		this.units.add(unit.getId());
	}
}
