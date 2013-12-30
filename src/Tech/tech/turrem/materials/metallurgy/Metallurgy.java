package tech.turrem.materials.metallurgy;

import zap.turrem.tech.TechBase;

public class Metallurgy extends TechBase
{
	public Metallurgy(int pass)
	{
		super(pass);
	}

	@Override
	public void loadBranches()
	{

	}

	public static int numPass()
	{
		return 5;
	}

	@Override
	public String getName()
	{
		switch (this.pass)
		{
			case 0:
				return "Shiny Rocks";
			case 1:
				return "Metallurgy";
			case 2:
				return "Alloys";
			case 3:
				return "Complex Metallurgy";
			case 4:
				return "Metal Working";
			default:
				return "Metallurgy";
		}
	}
}
