package zap.turrem.server.entity;

import zap.turrem.core.entity.Entity;
import zap.turrem.server.world.WorldServer;

public abstract class EntityServer extends Entity
{
	protected double posX;
	protected double posY;
	protected double posZ;
	
	protected float yaw;
	
	protected long age;
	
	protected WorldServer theWorld;
	
	protected int id;
	
	public EntityServer(WorldServer world)
	{
		super();
		this.theWorld = world;
		this.id = this.theWorld.addEntity(this);
	}
	
	public void onTick()
	{
		super.onTick();
		this.age++;
		this.onEntityTick();
	}
	
	public void onEntityTick()
	{
		
	}
	
	public void setPosition(double x, double y, double z)
	{
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		super.setPosition(x, y, z);
	}

	public final int getId()
	{
		return id;
	}
}
