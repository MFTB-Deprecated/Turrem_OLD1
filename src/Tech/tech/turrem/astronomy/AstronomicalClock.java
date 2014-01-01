package tech.turrem.astronomy;

import tech.turrem.clocks.MechanicalClock;
import branch.turrem.BranchAvailable;
import zap.turrem.tech.TechBase;

public class AstronomicalClock extends TechBase
{
	public AstronomicalClock(int pass)
	{
		super(pass);
	}

	@Override
	public void loadBranches()
	{
		(new BranchAvailable(this)).addRequired(MechanicalClock.class, 0).addRequired(Orrery.class, 1).push();
	}

	@Override
	public String getName()
	{
		return "Astronomical Clock";
	}
}
