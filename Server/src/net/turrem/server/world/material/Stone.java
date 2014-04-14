package net.turrem.server.world.material;

public class Stone extends Material
{
	public Stone(String name)
	{
		super(name);
	}

	@Override
	public int getColor()
	{
		return 0xA0A0A0;
	}
}
