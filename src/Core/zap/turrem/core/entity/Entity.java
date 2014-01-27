package zap.turrem.core.entity;

import zap.turrem.utils.geo.Box;

public abstract class Entity
{
	protected Box boundingBox;

	public float width;
	public float length;
	public float height;

	public float offx;
	public float offy;
	public float offz;

	public short rotation = 0;

	public Entity()
	{
		this.boundingBox = Box.getBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	}

	public void setPosition(double x, double y, double z)
	{
		float w;
		float l;
		float ox;
		float oz;
		float h = this.height;
		float oy = this.offy;
		if (this.rotation % 2 == 0)
		{
			w = this.width;
			l = this.length;
			ox = this.offx;
			oz = this.offz;
		}
		else
		{
			l = this.width;
			w = this.length;
			oz = this.offx;
			ox = this.offz;
		}
		if (this.rotation % 4 > 1)
		{
			ox = -ox - w;
			oz = -oz - l;
		}
		this.setBox(x + ox, x + ox + w, y + oy, y + oy + h, z + oz, z + oz + l);
	}

	public void setBox(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax)
	{
		this.boundingBox.setBoundsThis(xmin, ymin, zmin, xmax, ymax, zmax);
	}
	
	public void setBounds(Box box)
	{
		this.width = (float) box.getXLength();
		this.height = (float) box.getYLength();
		this.length = (float) box.getZLength();
		this.offx = (float) box.minX;
		this.offy = (float) box.minY;
		this.offz = (float) box.minZ;
	}

	public void onTick()
	{

	}

	public Box getBoundingBox()
	{
		return this.boundingBox;
	}
}
