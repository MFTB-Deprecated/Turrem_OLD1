package tech.turrem.writing;

import tech.turrem.language.WrittenLanguage;
import tech.turrem.materials.plant.Fiber;
import tech.turrem.tools.StoneTools;
import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.BranchAvailable;

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
			this.addBranch(new BranchAvailable(this.getIndex(ClayTablet.class, 1), this.getIndex(StoneTools.class, 1)));
		}
		if (this.pass == 1)
		{
			this.addBranch(new BranchAvailable(this.getIndex(Paper.class, 0), this.getIndex(Ink.class, 1), this.getIndex(WrittenLanguage.class, 1), this.getIndex(Fiber.class, 0)));
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
