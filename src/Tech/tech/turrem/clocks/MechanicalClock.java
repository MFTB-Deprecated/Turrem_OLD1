package tech.turrem.clocks;

import zap.turrem.tech.TechBase;

public class MechanicalClock extends TechBase
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
				return "Simple Mechanical Clock";
			case 1:
				return "Spring-driven Clock";
			case 2:
				return "Pendulum Clock";
			default:
				return "Mechanical Clock";
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