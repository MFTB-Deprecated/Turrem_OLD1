package zap.turrem.client.game.entity;

import zap.turrem.core.entity.Entity;
import zap.turrem.utils.geo.Point;

public class EntityClient extends Entity
{
	public boolean isDead = false;
	public boolean isAppear = true;
	
	protected double posX;
	protected double posY;
	protected double posZ;
	
	public Point start;
	public Point end;
	public int progress;
	public int time;
	public boolean inMotion = false;
	
	public void disappear()
	{
		this.isAppear = false;
	}
	
	public void kill()
	{
		this.isDead = true;
	}
	
	public void onTick()
	{
		super.onTick();
	}
	
	public void doMotion()
	{
		if (this.inMotion)
		{
			float slide = (float) (this.progress++) / (float) this.time;
			if (slide >= 1.0F)
			{
				this.cancelMotion();
			}
			Point p = Point.getSlide(this.start, this.end, slide);
			this.move(p);
		}
	}
	
	public void cancelMotion()
	{
		this.inMotion = false;
		this.progress = 0;
	}
	
	public void setPosition(double x, double y, double z)
	{
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		super.setPosition(x, y, z);
	}
	
	public void move(Point p)
	{
		this.setPosition(p.xCoord, p.yCoord, p.zCoord);
	}
}