package zap.turrem.utils.geo;

public class Ray
{
	public Point start;
	public Point end;
	
	public static Ray getRay(Point start, Point end)
	{
		return new Ray(start, end);
	}
	
	private Ray(Point p1, Point p2)
	{
		this.start = p1;
		this.end = p2;
	}
	
	public Ray setLength(double length)
	{
		Point e = Point.getSlideWithLength(this.start, this.end, length);
		this.end = e;
		return this;
	}
	
	public Box getBox()
	{
		return Box.getBox(this.start.xCoord, this.start.yCoord, this.start.zCoord, this.start.xCoord, this.start.yCoord, this.start.zCoord).eat(this.end);
	}
}
