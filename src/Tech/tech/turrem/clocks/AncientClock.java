package tech.turrem.clocks;

import zap.turrem.core.tech.TechBase;

public class AncientClock extends TechBase
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
				return "Sundials";
			case 1:
				return "Candles as Clocks";
			case 2:
				return "Hourglasses";
			default:
				return "Ancient Clocks";
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
