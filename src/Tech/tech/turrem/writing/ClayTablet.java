package tech.turrem.writing;

import branch.turrem.BranchAvailable;
import tech.turrem.language.WrittenLanguage;
import tech.turrem.tools.StoneTools;
import zap.turrem.core.tech.TechBase;

public class ClayTablet extends TechBase
{
	@Override
	public void loadBranches(int pass)
	{
		if (pass == 0)
		{
			(new BranchAvailable(this, pass)).addRequired(WrittenLanguage.class, 0).addRequired(StoneTools.class, 2).push();
		}
		if (pass == 1)
		{
			(new BranchAvailable(this, pass)).addRequired(ClayTablet.class, 0).addRequired(StoneTools.class, 1).push();
		}
	}

	@Override
	public String getName(int pass)
	{
		switch (pass)
		{
			case 0:
				return "Stone Tablet";
			case 1:
				return "Clay Tablet";
			default:
				return "Tablet";
		}
	}

	@Override
	public boolean isEntryLevel(int pass)
	{
		return false;
	}

	@Override
	public int getPassCount()
	{
		return 2;
	}
}
