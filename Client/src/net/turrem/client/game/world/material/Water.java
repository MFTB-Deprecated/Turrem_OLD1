package net.turrem.client.game.world.material;

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
}