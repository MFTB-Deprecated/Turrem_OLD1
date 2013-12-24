package tech.turrem.materials.plant;

import zap.turrem.tech.TechBase;

public class Glue extends TechBase
{
	public Glue(int pass)
	{
		super(pass);
	}

	@Override
	public void loadBranches()
	{

	}

	public static int numPass()
	{
		return 3;
	}

	@Override
	public String getName()
	{
		switch (this.pass)
		{
			case 0:
				return "Tar";
			case 1:
				return "Resin";
			case 2:
				return "Glue";
			default:
				return "Glue";
		}
	}
}
