package net.turrem.server.world.material;

public class Grass extends Material
{
	public Grass(String name)
	{
		super(name);
	}

	@Override
	public int getColor()
	{
		return 0x42772C;
	}
	
	@Override
	public boolean canGrowTrees(float prob)
	{
		return prob > 0.2F;
	}
}
