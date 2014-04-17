package net.turrem.server.world.material;

import java.util.HashMap;

public abstract class Material
{
	public static HashMap<String, Material> list = new HashMap<String, Material>();
	
	public static Material stone = new Stone("stone");
	public static Material dirt = new Dirt("dirt");
	public static Material grass = new Grass("grass");
	
	public final String id;
	
	public Material(String id)
	{
		this.id = id;
		list.put(this.id, this);
	}
	
	public abstract int getColor();
	
	public int getNumId()
	{
		return this.id.hashCode();
	}
}
