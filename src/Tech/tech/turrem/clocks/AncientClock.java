package tech.turrem.clocks;

import zap.turrem.tech.TechBase;

public class AncientClock extends TechBase
{
	public AncientClock(int pass)
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
				return "Sundials";
			case 1:
				return "Candle Clock";
			case 2:
				return "Hourglass";
			default:
				return "Ancient Clock";
		}
	}
}
