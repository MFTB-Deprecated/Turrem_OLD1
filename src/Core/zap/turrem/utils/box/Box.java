package zap.turrem.utils.box;

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
    
    public Box setBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
    {
    	this.minX = minX;
    	this.minY = minY;
    	this.minZ = minZ;
    	this.maxX = maxX;
    	this.maxY = maxY;
    	this.maxZ = maxZ;
    	return this;
    }
}
