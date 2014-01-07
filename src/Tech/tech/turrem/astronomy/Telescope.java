package tech.turrem.astronomy;

import zap.turrem.tech.TechBase;

public class Telescope extends TechBase
{
	@Override
	public void loadBranches(int pass)
	{
		
	}

	@Override
	public String getName(int pass)
	{
		return "Telescopes";
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
