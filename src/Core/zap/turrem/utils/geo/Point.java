package zap.turrem.utils.geo;

public class Point
{
	public double xCoord;
	public double yCoord;
	public double zCoord;

	public static Point getPoint(double x, double y, double z)
	{
		return new Point(x, y, z);
	}

	private Point(double x, double y, double z)
	{
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
	}

	public Point setPoint(double x, double y, double z)
	{
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
		return this;
	}
	
	public void moveDelta(double dx, double dy, double dz)
	{
		this.xCoord += dx;
		this.yCoord += dy;
		this.zCoord += dz;
	}

	public double squareDistanceOrigin()
	{
		double xs = this.xCoord * this.xCoord;
		double ys = this.yCoord * this.yCoord;
		double zs = this.zCoord * this.zCoord;

		return xs + ys + zs;
	}

	public double distanceOrigin()
	{
		return Math.sqrt(this.squareDistanceOrigin());
	}

	public double distanceTo(Point p)
	{
		return distance(this, p);
	}

	public static double distance(Point p1, Point p2)
	{
		return Math.sqrt(squareDistance(p1, p2));
	}

	public double squareDistanceTo(Point p)
	{
		return squareDistance(this, p);
	}

	public static double squareDistance(Point p1, Point p2)
	{
		double x = p1.xCoord - p2.xCoord;
		double y = p1.yCoord - p2.yCoord;
		double z = p1.zCoord - p2.zCoord;

		double xs = x * x;
		double ys = y * y;
		double zs = z * z;

		return xs + ys + zs;
	}

	public static Point getIntermediateWithXValue(Point p1, Point p2, double xval)
	{
		double x = p2.xCoord - p1.xCoord;
		double y = p2.yCoord - p1.yCoord;
		double z = p2.zCoord - p1.zCoord;

		if (x * x < 1E-7D)
		{
			return null;
		}
		else
		{
			double weight = (xval - p1.xCoord) / x;
			return weight >= 0.0D && weight <= 1.0D ? new Point(p1.xCoord + x * weight, p1.yCoord + y * weight, p1.zCoord + z * weight) : null;
		}
	}
	
	public void addPoint(Point p)
	{
		this.xCoord += p.xCoord;
		this.yCoord += p.yCoord;
		this.zCoord += p.zCoord;
	}
	
	public static Point addVector(Point point, Vector vector)
	{
		return new Point(point.xCoord + vector.xpart, point.yCoord + vector.ypart, point.zCoord + vector.zpart);
	}
	
	public static Point getIntermediateWithYValue(Point p1, Point p2, double yval)
	{
		double x = p2.xCoord - p1.xCoord;
		double y = p2.yCoord - p1.yCoord;
		double z = p2.zCoord - p1.zCoord;

		if (y * y < 1E-7D)
		{
			return null;
		}
		else
		{
			double weight = (yval - p1.yCoord) / y;
			return weight >= 0.0D && weight <= 1.0D ? new Point(p1.xCoord + x * weight, p1.yCoord + y * weight, p1.zCoord + z * weight) : null;
		}
	}
	
	public static Point getSlideWithYValue(Point p1, Point p2, double yval)
	{
		double x = p2.xCoord - p1.xCoord;
		double y = p2.yCoord - p1.yCoord;
		double z = p2.zCoord - p1.zCoord;

		if (y * y < 1E-7D)
		{
			return null;
		}
		else
		{
			double weight = (yval - p1.yCoord) / y;
			return new Point(p1.xCoord + x * weight, p1.yCoord + y * weight, p1.zCoord + z * weight);
		}
	}

	
	public static Point getIntermediateWithZValue(Point p1, Point p2, double zval)
	{
		double x = p2.xCoord - p1.xCoord;
		double y = p2.yCoord - p1.yCoord;
		double z = p2.zCoord - p1.zCoord;

		if (z * z < 1E-7D)
		{
			return null;
		}
		else
		{
			double weight = (zval - p1.zCoord) / z;
			return weight >= 0.0D && weight <= 1.0D ? new Point(p1.xCoord + x * weight, p1.yCoord + y * weight, p1.zCoord + z * weight) : null;
		}
	}


	public static Point getSlide(Point p1, Point p2, double slide)
	{
		double x = p2.xCoord - p1.xCoord;
		double y = p2.yCoord - p1.yCoord;
		double z = p2.zCoord - p1.zCoord;

		return new Point(p1.xCoord + x * slide, p1.yCoord + y * slide, p1.zCoord + z * slide);
	}
	
	public static Point getSlideWithLength(Point p1, Point p2, double length)
	{
		double x = p2.xCoord - p1.xCoord;
		double y = p2.yCoord - p1.yCoord;
		double z = p2.zCoord - p1.zCoord;

		double l = distance(p1, p2);
		
		if (l < 1E-7D)
		{
			return null;
		}
		else
		{
			double weight = length / l;
			return new Point(p1.xCoord + x * weight, p1.yCoord + y * weight, p1.zCoord + z * weight);
		}
	}
}
