package net.turrem.utils.geo;

public class Ray
{
	public Point start;
	public Point end;

	public static Ray getRay(Point start, Point end)
	{
		return new Ray(start, end);
	}

	public static Ray getRay(Point start, Vector vector)
	{
		Point end = Point.addVector(start, vector);
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

	public Ray addLengthEnd(double length)
	{
		Point e = Point.getSlideAddLength(this.start, this.end, length);
		this.end = e;
		return this;
	}

	public Ray addLengthStart(double length)
	{
		Point s = Point.getSlideAddLength(this.end, this.start, length);
		this.start = s;
		return this;
	}

	public double getLengthSqr()
	{
		return Point.squareDistance(this.start, this.end);
	}

	public double getLength()
	{
		return Point.distance(this.start, this.end);
	}

	public Box getBox()
	{
		return Box.getBox(this.start.xCoord, this.start.yCoord, this.start.zCoord, this.start.xCoord, this.start.yCoord, this.start.zCoord).eat(this.end);
	}

	public Ray duplicate()
	{
		return new Ray(this.start.duplicate(), this.end.duplicate());
	}

	public Ray extendScale(float scale)
	{
		this.end = Point.getSlide(this.start, this.end, scale);
		return this;
	}

	public Point getPart(float loc)
	{
		return Point.getSlide(this.start, this.end, loc);
	}
}
