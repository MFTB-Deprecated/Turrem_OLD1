package zap.turrem.core.entity;

import zap.turrem.utils.box.Box;

public class Entity
{
	protected Box boundingBox;
	
	public float width;
	public float height;
	
	public Entity()
	{
		
	}
	
	public void setPosition(float x, float y, float z)
	{
		float w = this.width / 2.0F;
		this.setBox(x - w, x + w, y, y + this.height, z - w, z + w);
	}
	
	public void setBox(float xmin, float xmax, float ymin, float ymax, float zmin, float zmax)
	{
		
	}
}
