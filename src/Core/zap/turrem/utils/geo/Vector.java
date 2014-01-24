package zap.turrem.utils.geo;

public class Vector extends Point
{
	protected Vector(double x, double y, double z)
	{
		super(x, y, z);
	}
	
	public Vector normalize()
	{
		return this.scale(1.0D);
	}
	
	public double length()
	{
		return this.distanceOrigin();
	}
	
	public Vector scale(double length)
	{
		double l = length / this.length();
		this.xCoord *= l;
		this.yCoord *= l;
		this.zCoord *= l;
		return this;
	}
}
