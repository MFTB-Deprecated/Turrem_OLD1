package tech.turrem.writing;

import branch.turrem.BranchAvailable;
import tech.turrem.language.WrittenLanguage;
import tech.turrem.tools.StoneTools;
import zap.turrem.tech.TechBase;

public class ClayTablet extends TechBase
{
	public ClayTablet(int pass)
	{
		super(pass);
	}

	@Override
	public void loadBranches()
	{
		if (this.pass == 0)
		{
			(new BranchAvailable(this)).addRequired(WrittenLanguage.class, 0).addRequired(StoneTools.class, 2).push();
		}
		if (this.pass == 1)
		{
			(new BranchAvailable(this)).addRequired(ClayTablet.class, 0).addRequired(StoneTools.class, 1).push();
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
				return "Stone Tablet";
			case 1:
				return "Clay Tablet";
			default:
				return "Tablet";
		}
	}
}
