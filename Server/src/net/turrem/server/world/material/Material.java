package net.turrem.server.world.material;

import java.util.HashMap;

public abstract class Material
{
	public static HashMap<String, Material> list = new HashMap<String, Material>();
	
	public static Material stone = new Stone("stone");
	public static Material dirt = new Dirt("dirt");
	public static Material grass = new Grass("grass");
	public static Material water = new Water("water");
	public static Material sand = new Sand("sand");
	
	private static short nextNum = 0;
	
	public final String id;
	public final short num;
	
	public Material(String id)
	{
		this.id = id;
		this.num = nextNum++;
		list.put(this.id, this);
	}
	
	public abstract int getColor();
	
	public short getNumId()
	{
		return this.num;
	}
	
	public boolean canGrowTrees(float prob)
	{
		return false;
	}
}
