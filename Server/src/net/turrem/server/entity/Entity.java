package net.turrem.server.entity;

import java.util.Random;

import net.turrem.server.Realm;
import net.turrem.server.network.server.ServerPacket;
import net.turrem.server.world.World;
import net.turrem.utils.nbt.NBTCompound;

public abstract class Entity
{
	protected static long nextId = 0;

	public final long entityIdentifier;

	protected boolean living = false;
	public boolean shouldUnload = false;

	public World theWorld;

	public double x;
	public double y;
	public double z;

	protected int visibility = 0;
	protected int oldVisibility = 0;
	
	private Random entityRand;
	private Random worldRand;
	
	public int modelType = -1;

	public Entity()
	{
		this.entityIdentifier = nextId++;
		this.entityRand = new Random();
	}

	public void onTick()
	{
		this.oldVisibility = this.visibility;
		this.visibility = this.theWorld.storage.getVisibility(((int) this.x) << 4, ((int) this.z) << 4);

		{
			int vis = this.visibility;
			int oldvis = this.oldVisibility;

			for (int i = 0; i < 16; i++)
			{
				if ((vis & 1) < (oldvis & 1))
				{
					Realm realm = this.theWorld.getRealms()[i];
					if (realm != null)
					{
						realm.onEntityDisappear(this);
					}
				}
				if ((vis & 1) > (oldvis & 1))
				{
					Realm realm = this.theWorld.getRealms()[i];
					if (realm != null)
					{
						realm.onEntityAppear(this);
					}
				}
				vis >>>= 1;
				oldvis >>>= 1;
			}
		}
	}

	public void setVisibility(int realm, boolean visible)
	{
		int mod = 1;
		mod <<= realm;

		if (visible)
		{
			this.visibility |= mod;
		}
		else
		{
			this.visibility &= ~mod;
		}
	}
	
	public void sendPacketToClients(ServerPacket packet)
	{
		if (this.theWorld == null)
		{
			return;
		}
		
		int vis = this.visibility;

		for (int i = 0; i < 16; i++)
		{
			if ((vis & 1) == 1)
			{
				Realm realm = this.theWorld.getRealms()[i];
				if (realm != null)
				{
					realm.sendPacket(packet);
				}
			}
			vis >>>= 1;
		}
	}

	public boolean getVisibility(int realm)
	{
		int vis = this.visibility;
		vis >>>= realm;
		vis &= 1;
		return vis == 1;
	}

	public Random getRNG()
	{
		return this.entityRand;
	}
	
	public Random getWorldLinkedRNG()
	{
		return this.worldRand;
	}
	
	public abstract String getEntityType();
	
	public void writeNBT(NBTCompound data)
	{
		data.setInt("ModelNumber", this.modelType);
	}

	public void onWorldRegister(World world)
	{
		this.living = true;
		this.theWorld = world;
		this.worldRand = new Random(this.getWorldLinkedSeed(this.theWorld.seed));
		this.modelType = this.worldRand.nextInt();
		this.onEnter();
	}
	
	private long getWorldLinkedSeed(long seed)
	{
		seed ^= this.getEntityType().hashCode();
		seed *= ((long) (this.x * 16)) ^ ((long) (this.z * 16));
		return seed;
	}

	public void kill()
	{
		this.living = false;
	}

	public boolean isDead()
	{
		return !this.living;
	}

	public void unload()
	{
		this.shouldUnload = true;
	}

	public abstract float veiwDistance();

	public abstract void onEnter();

	public abstract short loadRadius();

	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof Entity)
		{
			return ((Entity) obj).entityIdentifier == this.entityIdentifier;
		}
		return false;
	}
}
