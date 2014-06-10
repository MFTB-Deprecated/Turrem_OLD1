package net.turrem.server.world.material;

public class Water extends Material
{
	public Water(String id)
	{
		super(id);
	}

	@Override
	public int getColor()
	{
		return 0x3330FF;
	}
	
	@Override
	public boolean isPlayerSpawnable()
	{
		return false;
	}
}
