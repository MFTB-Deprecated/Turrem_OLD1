package tech.turrem.materials.metallurgy;

import zap.turrem.tech.TechBase;

public class Metallurgy extends TechBase
{
	@Override
	public void loadBranches(int pass)
	{

	}

	@Override
	public String getName(int pass)
	{
		switch (pass)
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

	@Override
	public boolean isEntryLevel(int pass)
	{
		return false;
	}

	@Override
	public int getPassCount()
	{
		return 5;
	}
}
