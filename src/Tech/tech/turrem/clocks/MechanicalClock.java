package tech.turrem.clocks;

import zap.turrem.tech.TechBase;

public class MechanicalClock extends TechBase
{
	public MechanicalClock(int pass)
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
				return "Simple Mechanical Clock";
			case 1:
				return "Spring-driven Clock";
			case 2:
				return "Pendulum Clock";
			default:
				return "Mechanical Clock";
		}
	}
}