package tech.turrem.astronomy;

import branch.turrem.BranchAvailable;
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
		if (this.pass == 2)
		{
			(new BranchAvailable(this)).addRequired(Orrery.class, 1).push();
		}
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
