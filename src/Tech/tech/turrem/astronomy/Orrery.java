package tech.turrem.astronomy;

import zap.turrem.tech.TechBase;

public class Orrery extends TechBase
{
	public Orrery(int pass)
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
				return "Armillary sphere";
			case 1:
				return "Astrarium";
			case 2:
				return "Orrery";
			default:
				return "Orrery";
		}
	}

}
