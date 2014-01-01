package tech.turrem.astronomy;

import branch.turrem.BranchAvailable;
import zap.turrem.tech.TechBase;

public class Astrolabe extends TechBase
{
	public Astrolabe(int pass)
	{
		super(pass);
	}

	@Override
	public void loadBranches()
	{
		if (this.pass == 1)
		{
			(new BranchAvailable(this)).addRequired(Astrolabe.class, 0).addRequired(Sextant.class, 1).push();
		}
	}
	
	public static int numPass()
	{
		return 2;
	}

	@Override
	public String getName()
	{
		switch (this.pass)
		{
			case 0:
				return "Planisphere";
			case 1:
				return "Astrolabe";
			default:
				return "Astrolabe";
		}
	}
}
