package tech.turrem.astronomy;

import branch.turrem.BranchAvailable;
import zap.turrem.tech.TechBase;

public class Sextant extends TechBase
{
	public Sextant(int pass)
	{
		super(pass);
	}

	@Override
	public void loadBranches()
	{
		if (this.pass == 1)
		{
			(new BranchAvailable(this)).addRequired(Sextant.class, 0).push();
		}
		if (this.pass == 2)
		{
			(new BranchAvailable(this)).addRequired(Sextant.class, 1).push();
		}
		if (this.pass == 3)
		{
			(new BranchAvailable(this)).addRequired(Sextant.class, 1).addRequired(Telescope.class, 0).push();
		}
	}

	public static int numPass()
	{
		return 4;
	}

	@Override
	public String getName()
	{
		switch (this.pass)
		{
			case 0:
				return "Mural Instrument";
			case 1:
				return "Dioptra";
			case 2:
				return "Sextant";
			case 3:
				return "Navigational Sextant";
			default:
				return "Sextant";
		}
	}
}
