package tech.turrem.astronomy;

import tech.turrem.clocks.MechanicalClock;
import branch.turrem.BranchAvailable;
import zap.turrem.tech.TechBase;

public class AstronomicalClock extends TechBase
{
	@Override
	public void loadBranches(int pass)
	{
		(new BranchAvailable(this, pass)).addRequired(MechanicalClock.class, 0).addRequired(Orrery.class, 1).push();
	}

	@Override
	public String getName(int pass)
	{
		return "Astronomical Clock";
	}

	@Override
	public boolean isEntryLevel(int pass)
	{
		return false;
	}

	@Override
	public int getPassCount()
	{
		return 1;
	}
}
