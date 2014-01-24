package zap.turrem.utils.geo;

public class Vector
{
	public float xpart;
	public float ypart;
	public float zpart;
	
	public static Vector getVector(float xpart, float ypart, float zpart)
	{
		return new Vector(xpart, ypart, zpart);
	}
	
	public static Vector getVector(Point point)
	{
		return new Vector((float) point.xCoord, (float) point.yCoord, (float) point.zCoord);
	}
	
	public static Vector getVector(Point p1, Point p2)
	{
		return new Vector((float) (p2.xCoord - p1.xCoord), (float) (p2.yCoord - p1.yCoord), (float) (p2.zCoord - p1.zCoord));
	}
	
	public static Vector getVector(Ray ray)
	{
		return getVector(ray.start, ray.end);
	}
	
	private Vector(float x, float y, float z)
	{
		this.xpart = x;
		this.ypart = y;
		this.zpart = z;
	}
	
	public float lengthSqr()
	{
		return this.xpart * this.xpart + this.ypart * this.ypart + this.zpart * this.zpart;
	}
	
	public float length()
	{
		return (float) Math.sqrt(this.lengthSqr());
	}
	
	public void normalize()
	{
		this.changeLength(1.0F);
	}
	
	public void changeLength(float length)
	{
		this.scale(length / this.length());
	}
	
	public void scale(float scale)
	{
		this.xpart *= scale;
		this.ypart *= scale;
		this.zpart *= scale;
	}
	
	public static Vector cross(Vector a, Vector b)
	{
		float x = a.ypart * b.zpart - a.zpart * b.ypart;
		float y = a.zpart * b.xpart - a.xpart * b.zpart;
		float z = a.xpart * b.ypart - a.ypart * b.xpart;
		
		return new Vector(x, y, z);
	}
} 
