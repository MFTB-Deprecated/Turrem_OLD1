package net.turrem.client.game.world.material;

import java.util.HashMap;

public abstract class Material
{
	public static HashMap<String, Material> list = new HashMap<String, Material>();
	public static HashMap<Short, String> numidmap = new HashMap<Short, String>();
	
	public static Material stone = new Stone("stone");
	public static Material dirt = new Dirt("dirt");
	public static Material grass = new Grass("grass");
	public static Material water = new Water("water");
	public static Material sand = new Sand("sand");

	public final String id;

	public Material(String id)
	{
		this.id = id;
		list.put(this.id, this);
	}

	public abstract int getColor();
	
	public static Material getMaterial(short num)
	{
		String id = numidmap.get(num);
		if (id == null)
		{
			return null;
		}
		return list.get(id);
	}
}
