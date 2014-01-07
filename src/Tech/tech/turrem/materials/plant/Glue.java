package tech.turrem.materials.plant;

import zap.turrem.tech.TechBase;

public class Glue extends TechBase
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
				return "Tar";
			case 1:
				return "Resin";
			case 2:
				return "Glue";
			default:
				return "Glue";
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
		return 3;
	}
}
