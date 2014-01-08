package tech.turrem.clocks;

import zap.turrem.core.tech.TechBase;

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
				return "Mechanical Clocks";
			case 1:
				return "Spring-driven Clocks";
			case 2:
				return "Pendulum Clocks";
			default:
				return "Mechanical Clocks";
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