package zap.turrem.utils.geo;

public class Box
{
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;
    
    public static Box getBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
    {
    	return new Box(minX, minY, minZ, maxX, maxY, maxZ);
    }
    
    private Box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
    {
    	this.minX = minX;
    	this.minY = minY;
    	this.minZ = minZ;
    	this.maxX = maxX;
    	this.maxY = maxY;
    	this.maxZ = maxZ;
    }
    
    public Box setBoundsThis(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
    {
    	this.minX = minX;
    	this.minY = minY;
    	this.minZ = minZ;
    	this.maxX = maxX;
    	this.maxY = maxY;
    	this.maxZ = maxZ;
    	return this;
    }
    
    public Box setBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
    {
    	return this.duplicate().setBoundsThis(minX, minY, minZ, maxX, maxY, maxZ);
    }
    
    public Box duplicate()
    {
    	return new Box(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }
    
    public Box expandThis(float size)
    {
    	this.maxX += size;
    	this.maxY += size;
    	this.maxZ += size;
       	this.minX -= size;
    	this.minY -= size;
    	this.minZ -= size;
    	return this;
    }
    
    public Box expand(float size)
    {
    	return this.duplicate().expandThis(size);
    }
    
    public Box growThis(float x, float y, float z)
    {
    	if (x > 0)
    	{
    		this.maxX += x;
    	}
    	else
    	{
    		this.minX += x;
    	}
    	if (y > 0)
    	{
    		this.maxY += y;
    	}
    	else
    	{
    		this.minY += y;
    	}
    	if (z > 0)
    	{
    		this.maxZ += z;
    	}
    	else
    	{
    		this.minZ += z;
    	}
    	return this;
    }
    
    public Box grow(float x, float y, float z)
    {
    	return this.duplicate().growThis(x, y, z);
    }
    
    public Box eatThis(Point p)
    {
    	double x = p.xCoord;
    	if (x < this.minX)
    	{
    		this.minX = x;
    	}
    	if (x > this.maxX)
    	{
    		this.maxX = x;
    	}
    	double y = p.yCoord;
    	if (y < this.minY)
    	{
    		this.minY = y;
    	}
    	if (y > this.maxY)
    	{
    		this.maxY = y;
    	}
    	double z = p.zCoord;
    	if (z < this.minZ)
    	{
    		this.minZ = z;
    	}
    	if (z > this.maxZ)
    	{
    		this.maxZ = z;
    	}
    	return this;
    }
    
    public Box eat(Point p)
    {
    	return this.duplicate().eatThis(p);
    }
    
    public boolean isPointInside(Point point)
    {
        return point.xCoord > this.minX && point.xCoord < this.maxX ? (point.yCoord > this.minY && point.yCoord < this.maxY ? point.zCoord > this.minZ && point.zCoord < this.maxZ : false) : false;
    }
    
    public boolean isPointInYZ(Point point)
    {
        return point == null ? false : point.yCoord >= this.minY && point.yCoord <= this.maxY && point.zCoord >= this.minZ && point.zCoord <= this.maxZ;
    }

    public boolean isPointInXZ(Point point)
    {
        return point == null ? false : point.xCoord >= this.minX && point.xCoord <= this.maxX && point.zCoord >= this.minZ && point.zCoord <= this.maxZ;
    }

    public boolean isPointInXY(Point point)
    {
        return point == null ? false : point.xCoord >= this.minX && point.xCoord <= this.maxX && point.yCoord >= this.minY && point.yCoord <= this.maxY;
    }
    
    public boolean intersectsWith(Box box)
    {
        return box.maxX > this.minX && box.minX < this.maxX ? (box.maxY > this.minY && box.minY < this.maxY ? box.maxZ > this.minZ && box.minZ < this.maxZ : false) : false;
    }
    
    public static boolean intersects(Box box1, Box box2)
    {
    	return box1.intersectsWith(box2);
    }
    
    public BoxPin calculateIntercept(Point point1, Point point2)
    {
        Point xdown = Point.getIntermediateWithXValue(point1, point2, this.minX);
        Point xup = Point.getIntermediateWithXValue(point1, point2, this.maxX);
        Point ydown = Point.getIntermediateWithYValue(point1, point2, this.minY);
        Point yup = Point.getIntermediateWithYValue(point1, point2, this.maxY);
        Point zdown = Point.getIntermediateWithZValue(point1, point2, this.minZ);
        Point zup = Point.getIntermediateWithZValue(point1, point2, this.maxZ);

        if (!this.isPointInYZ(xdown))
        {
            xdown = null;
        }

        if (!this.isPointInYZ(xup))
        {
            xup = null;
        }

        if (!this.isPointInXZ(ydown))
        {
            ydown = null;
        }

        if (!this.isPointInXZ(yup))
        {
            yup = null;
        }

        if (!this.isPointInXY(zdown))
        {
            zdown = null;
        }

        if (!this.isPointInXY(zup))
        {
            zup = null;
        }

        Point pin = null;

        if (xdown != null && (pin == null || point1.squareDistanceTo(xdown) < point1.squareDistanceTo(pin)))
        {
            pin = xdown;
        }

        if (xup != null && (pin == null || point1.squareDistanceTo(xup) < point1.squareDistanceTo(pin)))
        {
            pin = xup;
        }

        if (ydown != null && (pin == null || point1.squareDistanceTo(ydown) < point1.squareDistanceTo(pin)))
        {
            pin = ydown;
        }

        if (yup != null && (pin == null || point1.squareDistanceTo(yup) < point1.squareDistanceTo(pin)))
        {
            pin = yup;
        }

        if (zdown != null && (pin == null || point1.squareDistanceTo(zdown) < point1.squareDistanceTo(pin)))
        {
            pin = zdown;
        }

        if (zup != null && (pin == null || point1.squareDistanceTo(zup) < point1.squareDistanceTo(pin)))
        {
            pin = zup;
        }

        if (pin == null)
        {
            return null;
        }
        else
        {
            EnumDir dir = null;

            if (pin == xdown)
            {
                dir = EnumDir.XDown;
            }

            if (pin == xup)
            {
            	dir = EnumDir.XUp;
            }

            if (pin == ydown)
            {
            	dir = EnumDir.YDown;
            }

            if (pin == yup)
            {
            	dir = EnumDir.YUp;
            }

            if (pin == zdown)
            {
            	dir = EnumDir.ZDown;
            }

            if (pin == zup)
            {
            	dir = EnumDir.ZUp;
            }

            return new BoxPin(this, dir, pin);
        }
    }
}
