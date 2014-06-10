package net.turrem.server.entity.unit;

import net.turrem.server.Realm;
import net.turrem.server.entity.IHolding;
import net.turrem.server.entity.SoftEntity;
import net.turrem.server.load.control.GameEntity;
import net.turrem.server.network.server.ServerPacketMoveEntity;

@GameEntity(from = "turrem", author = "eekysam")
public abstract class EntityUnit extends SoftEntity implements IHolding
{
	protected Realm allegiance = null;
	
	public Realm getAllegiance()
	{
		return this.allegiance;
	}
	
	public Realm setAllegiance(Realm newrealm)
	{
		Realm oldrealm = this.allegiance;
		if (oldrealm != null)
		{
			oldrealm.leaveRealm(this, "CHANGED");
		}
		this.allegiance = newrealm;
		if (newrealm != null)
		{
			newrealm.joinRealm(this);
		}
		return oldrealm;
	}
	
	public boolean getRealmVisibility(int x, int z)
	{
		int cx = x >> 4;
		int cz = z >> 4;
		
		if (this.allegiance == null)
		{
			return false;
		}
		
		return this.theWorld.storage.getVisibility(cx, cz, this.allegiance.realmId);
	}
	
	public boolean getThisVisibility(int x, int z)
	{
		float dx = (float) x - (float) this.x;
		float dz = (float) z - (float) this.z;
		
		return dx * dx + dz * dz <= this.veiwDistance();
	}
	
	@Override
	public void clientMove(Realm realm, float x, float z)
	{
		if (this.allegiance != null && realm.realmId == this.allegiance.realmId)
		{
			double dx = x - this.x;
			double dz = z - this.z;
			double d = Math.sqrt(dz * dz + dx * dx);
			this.x += dx / d;
			this.z += dz / d;
			this.y = this.theWorld.getHeight((int) this.x, (int) this.z);
			ServerPacketMoveEntity move = new ServerPacketMoveEntity();
			move.xpos = (float) this.x;
			move.ypos = (float) this.y;
			move.zpos = (float) this.z;
			move.entity = this.entityIdentifier;
			move.movetime = 15;
			this.sendPacketToClients(move);
		}
	}
}
