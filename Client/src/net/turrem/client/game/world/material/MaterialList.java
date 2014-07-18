package net.turrem.client.game.world.material;

import java.util.HashMap;

import net.turrem.client.network.server.ServerPacketMaterialSync;

public class MaterialList
{
	protected static HashMap<Short, Material> mats = new HashMap<Short, Material>();
	private static Material brokenMat = new Material();
	
	public static Material getMaterial(short num)
	{
		Material mat = mats.get(num);
		if (mat != null)
		{
			return mat;
		}
		return brokenMat;
	}

	public static void put(ServerPacketMaterialSync sync)
	{
		Material newmat = new Material();
		newmat.red = sync.r;
		newmat.green = sync.g;
		newmat.blue = sync.b;
		newmat.name = sync.name;
		mats.put(sync.num, newmat);
	}
}
