package net.turrem.app.server.world.material;

import java.util.ArrayList;

public class MaterialList
{
	public static ArrayList<Material> list = new ArrayList<Material>();
	
	public static Material greyStone = new Material("Stone", 0xD1D1D1);
	public static Material dryDirt = new Material("Dirt", 0x9E8565);
	public static Material lowGrass = new Material("Grass", 0x21B01C);
	public static Material highGrass = new Material("Grass", 0x69AD66);
	public static Material dryGrass = new Material("Grass", 0xEEF2A7);
	public static Material whiteSand = new Material("Sand", 0xECEDD3);
	public static Material normalSand = new Material("Sand", 0xE8D756);
	public static Material dirtySand = new Material("Sand", 0xA8A05E);
	public static Material mountainSnow = new Material("Snow", 0xE8E8E8);
	public static LiquidMaterial oceanWater = new LiquidMaterial("Water", 0x0A1391);
}
