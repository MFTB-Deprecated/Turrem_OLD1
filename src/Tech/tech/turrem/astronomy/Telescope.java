package tech.turrem.astronomy;

import zap.turrem.tech.TechBase;

public class Telescope extends TechBase
{
	public Telescope(int pass)
	{
		super(pass);
	}

	@Override
	public void loadBranches()
	{
		
	}

	@Override
	public String getName()
	{
		return "Telescope";
	}
}
