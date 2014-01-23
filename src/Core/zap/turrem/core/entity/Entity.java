package zap.turrem.core.entity;

import zap.turrem.utils.box.Box;

public abstract class Entity
{
	protected Box boundingBox;
	
	public float width;
	public float height;
	
	public Entity()
	{
		this.boundingBox = Box.getBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	}
	
	public void setPosition(double x, double y, double z)
	{
		float w = this.width / 2.0F;
		this.setBox(x - w, x + w, y, y + this.height, z - w, z + w);
	}
	
	public void setBox(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax)
	{
		this.boundingBox.setBounds(xmin, ymin, zmin, xmax, ymax, zmax);
	}
	
	public void onTick()
	{
		
	}
}
