package zap.turrem.server.entity;

import zap.turrem.core.entity.Entity;
import zap.turrem.server.world.World;

public abstract class EntityMP extends Entity
{
	protected double posX;
	protected double posY;
	protected double posZ;
	
	protected float yaw;
	
	protected long age;
	
	protected World theWorld;
	
	protected int id;
	
	public EntityMP(World world)
	{
		super();
		this.theWorld = world;
		this.id = this.theWorld.addEntity(this);
	}
	
	public void onTick()
	{
		this.age++;
		this.onEntityTick();
	}
	
	public void onEntityTick()
	{
		
	}
	
	public void setPosition(float x, float y, float z)
	{
		this.posX = x;
		this.posY = y;
		this.posZ = z;
	}

	public final int getId()
	{
		return id;
	}
}
